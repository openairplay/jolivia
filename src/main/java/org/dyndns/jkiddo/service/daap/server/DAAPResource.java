package org.dyndns.jkiddo.service.daap.server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationMethod;
import org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationSchemes;
import org.dyndns.jkiddo.dmap.chunks.dmap.DatabaseCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemName;
import org.dyndns.jkiddo.dmap.chunks.dmap.LoginRequired;
import org.dyndns.jkiddo.dmap.chunks.dmap.ServerInfoResponse;
import org.dyndns.jkiddo.dmap.chunks.dmap.Status;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsAutoLogout;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsBrowse;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsExtensions;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsIndex;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsPersistentIds;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsQuery;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsUpdate;
import org.dyndns.jkiddo.dmap.chunks.dmap.TimeoutInterval;
import org.dyndns.jkiddo.guice.JoliviaListener;
import org.dyndns.jkiddo.service.dmap.DMAPResource;
import org.dyndns.jkiddo.service.dmap.Util;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DAAPResource extends DMAPResource implements IMusicLibrary
{
	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";
	public static final String DAAP_RESOURCE = "DAAP_IMPLEMENTATION";

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DATABASE_ID_KEY = "Database ID";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String MACHINE_NAME_KEY = "Machine Name";
	private static final String ITSH_VERSION_KEY = "iTSh Version";
	private static final String DAAP_VERSION_KEY = "Version";
	private static final String PASSWORD_KEY = "Password";

	@Inject
	public DAAPResource(JmmDNS mDNS, @Named(DAAP_PORT_NAME) Integer port, @Named(JoliviaListener.APPLICATION_NAME) String applicationName, MusicItemManager itemManager) throws IOException
	{
		super(mDNS, port, itemManager);
		this.name = applicationName;
		this.signUp();
	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		final String hash;
		try {
			hash = Jolivia.toHex(hostname.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DATABASE_ID_KEY, hash);
		records.put(MACHINE_ID_KEY, hash);
		records.put(MACHINE_NAME_KEY, hostname);
		records.put(ITSH_VERSION_KEY, DmapUtil.MUSIC_SHARING_VERSION_201 + "");
		records.put(DAAP_VERSION_KEY, DmapUtil.APRO_VERSION_3011 + "");
		records.put(PASSWORD_KEY, "0");
		return ServiceInfo.create(DAAP_SERVICE_TYPE, name, port, 0, 0, records);
	}

	@Override
	@Path("server-info")
	@GET
	public Response serverInfo(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info) throws IOException
	{
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();

		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(itemManager.getDmapProtocolVersion());
		serverInfoResponse.add(itemManager.getDaapProtocolVersion());
		serverInfoResponse.add(itemManager.getDpapProtocolVersion());
		serverInfoResponse.add(new ItemName(name));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(true));

		// serverInfoResponse.add(new
		// MusicSharingVersion(DaapUtil.MUSIC_SHARING_VERSION_201));

		// NOTE: the value of the following boolean chunks does not matter!
		// They are either present (=true) or not (=false).

		// client should perform /login request (create session)

		serverInfoResponse.add(new LoginRequired(true));
		serverInfoResponse.add(new SupportsExtensions(true));
		serverInfoResponse.add(new SupportsIndex(true));
		serverInfoResponse.add(new SupportsBrowse(true));
		serverInfoResponse.add(new SupportsQuery(true));
		serverInfoResponse.add(new SupportsPersistentIds(true));

		PasswordMethod authenticationMethod = itemManager.getAuthenticationMethod();
		if(!(authenticationMethod == PasswordMethod.NO_PASSWORD))
		{
			if(authenticationMethod == PasswordMethod.PASSWORD)
			{
				serverInfoResponse.add(new AuthenticationMethod(PasswordMethod.PASSWORD));
			}
			else
			{
				serverInfoResponse.add(new AuthenticationMethod(PasswordMethod.USERNAME_AND_PASSWORD));
			}

			serverInfoResponse.add(new AuthenticationSchemes(AuthenticationSchemes.BASIC_SCHEME | AuthenticationSchemes.DIGEST_SCHEME));
		}
		else
		{
			serverInfoResponse.add(new AuthenticationMethod(AuthenticationMethod.PasswordMethod.NO_PASSWORD));
		}

		serverInfoResponse.add(new DatabaseCount(itemManager.getDatabases().size()));
		serverInfoResponse.add(new SupportsUpdate(true));

		return Util.buildResponse(serverInfoResponse, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception
	{
		File file = itemManager.getItemAsFile(databaseId, itemId);

		long[] range = getRange(rangeHeader, 0, file.length());
		int pos = (int) range[0];
		int end = (int) range[1];
		RandomAccessFile raf = new RandomAccessFile(file, "r");

		byte[] buffer = new byte[end - pos];
		raf.seek(pos);
		raf.readFully(buffer, 0, buffer.length);
		Closeables.closeQuietly(raf);
		return Util.buildAudioResponse(buffer, pos, file.length(), itemManager.getDMAPKey(), name);
	}

	static private long[] getRange(String rangeHeader, long position, long end)
	{
		if(!Strings.isNullOrEmpty(rangeHeader))
		{
			StringTokenizer token = new StringTokenizer(rangeHeader, "=");
			String key = token.nextToken().trim();

			if(("bytes").equals(key) == false)
			{
				throw new NullPointerException("Format of range header is unknown");
			}
			StringTokenizer rangesToken = new StringTokenizer(token.nextToken(), "-");
			position = Long.parseLong(rangesToken.nextToken().trim());
			if(rangesToken.hasMoreTokens())
				end = Long.parseLong(rangesToken.nextToken().trim());
		}

		return(new long[] { position, end });
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}/extra_data/artwork")
	@GET
	public Response artwork(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("mw") String mw, @QueryParam("mh") String mh) throws Exception
	{
		throw new NotImplementedException();
	}

}
