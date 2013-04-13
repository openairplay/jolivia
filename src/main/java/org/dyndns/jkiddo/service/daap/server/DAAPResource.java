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
import org.dyndns.jkiddo.dmap.chunks.daap.SupportsGroups;
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
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsResolve;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsUpdate;
import org.dyndns.jkiddo.dmap.chunks.dmap.TimeoutInterval;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownFP;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownFR;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownMA;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownMQ;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSE;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSL;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSR;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSX;
import org.dyndns.jkiddo.dmap.chunks.unknown.UnknownTr;
import org.dyndns.jkiddo.dmap.chunks.unknown.Unknowned;
import org.dyndns.jkiddo.dmap.chunks.unknown.Unknownml;
import org.dyndns.jkiddo.dmap.chunks.unknown.Unknowntc;
import org.dyndns.jkiddo.dmap.chunks.unknown.Unknownto;
import org.dyndns.jkiddo.guice.JoliviaListener;
import org.dyndns.jkiddo.service.dmap.DMAPResource;
import org.dyndns.jkiddo.service.dmap.Util;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DAAPResource extends DMAPResource<MusicItemManager> implements IMusicLibrary
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
	private final String serviceGuid;

	@Inject
	public DAAPResource(JmmDNS mDNS, @Named(DAAP_PORT_NAME) Integer port, @Named(JoliviaListener.APPLICATION_NAME) String applicationName, MusicItemManager itemManager) throws IOException
	{
		super(mDNS, port, itemManager);
		this.name = applicationName;
		this.serviceGuid = JoliviaListener.toServiceGuid(applicationName);
		this.signUp();
		
	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		final String hexedHostname;
		try {
			hexedHostname = Jolivia.toHex(hostname.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(MACHINE_NAME_KEY, name);
		records.put("OSsi", "0x4E8DAC");
		records.put(PASSWORD_KEY, "0");
		records.put("Media Kinds Shared", "0");
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(MACHINE_ID_KEY, hexedHostname);		
		records.put(DAAP_VERSION_KEY, DmapUtil.APRO_VERSION_3011 + "");
		records.put(ITSH_VERSION_KEY, DmapUtil.MUSIC_SHARING_VERSION_309 + "");
		records.put("MID", "0x" + serviceGuid);
		records.put("dmc", "131081");
		records.put(DATABASE_ID_KEY, hexedHostname);
		
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
		serverInfoResponse.add(new ItemName(name));
		serverInfoResponse.add(itemManager.getDaapProtocolVersion());
		serverInfoResponse.add(itemManager.getMusicSharingVersion());
		serverInfoResponse.add(new SupportsExtensions(true));
		serverInfoResponse.add(new SupportsGroups(3));
		serverInfoResponse.add(new UnknownSE(0x80000));
		serverInfoResponse.add(new UnknownMQ(true));
		serverInfoResponse.add(new UnknownFR(0x64));
		serverInfoResponse.add(new UnknownTr(true));
		serverInfoResponse.add(new UnknownSL(true));
		serverInfoResponse.add(new UnknownSR(true));
		serverInfoResponse.add(new UnknownFP(2));
		serverInfoResponse.add(new UnknownSX(111));
		serverInfoResponse.add(itemManager.getProtocolVersion());
		serverInfoResponse.add(new Unknowned(true));
		Unknownml msml = new Unknownml();
		msml.add(new UnknownMA(0xBF940AB92600L)); //Totally unknown
		serverInfoResponse.add(msml);
		serverInfoResponse.add(new LoginRequired(true));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(true));
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
		serverInfoResponse.add(new SupportsUpdate(true));
		serverInfoResponse.add(new SupportsPersistentIds(true));
		serverInfoResponse.add(new SupportsExtensions(true));
		serverInfoResponse.add(new SupportsBrowse(true));
		serverInfoResponse.add(new SupportsQuery(true));
		serverInfoResponse.add(new SupportsIndex(true));
		serverInfoResponse.add(new SupportsResolve(true));
		serverInfoResponse.add(new DatabaseCount(itemManager.getDatabases().size()));
		serverInfoResponse.add(new Unknowntc(0x5169B375)); //Totally unknown
		serverInfoResponse.add(new Unknownto(7200)); 
		
		

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
