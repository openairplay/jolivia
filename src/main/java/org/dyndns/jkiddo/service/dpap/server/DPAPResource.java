package org.dyndns.jkiddo.service.dpap.server;

import java.io.IOException;
import java.util.HashMap;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.chunks.dmap.DatabaseCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemName;
import org.dyndns.jkiddo.dmap.chunks.dmap.LoginRequired;
import org.dyndns.jkiddo.dmap.chunks.dmap.ServerInfoResponse;
import org.dyndns.jkiddo.dmap.chunks.dmap.Status;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsAutoLogout;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsIndex;
import org.dyndns.jkiddo.dmap.chunks.dmap.TimeoutInterval;
import org.dyndns.jkiddo.guice.JoliviaListener;
import org.dyndns.jkiddo.service.dmap.DMAPResource;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DPAPResource extends DMAPResource implements IImageLibrary
{
	public static final String DPAP_SERVER_PORT_NAME = "DPAP_SERVER_PORT_NAME";
	public static final String DPAP_RESOURCE = "DPAP_IMPLEMENTATION";

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DPAP_VERSION_KEY = "Version";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String IPSH_VERSION_KEY = "iPSh Version";
	private static final String PASSWORD_KEY = "Password";
	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";

	@Inject
	public DPAPResource(JmmDNS mDNS, @Named(DPAP_SERVER_PORT_NAME) Integer port, @Named(JoliviaListener.APPLICATION_NAME) String applicationName, @Named(DPAP_RESOURCE) IItemManager itemManager) throws IOException
	{
		super(mDNS, port, itemManager);
		this.name = applicationName;
		this.signUp();
	}

	@Override
	@Path("server-info")
	@GET
	public Response serverInfo(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info) throws IOException
	{
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(itemManager.getDmapProtocolVersion());
		serverInfoResponse.add(itemManager.getDpapProtocolVersion());
		serverInfoResponse.add(new ItemName(name));
		serverInfoResponse.add(new LoginRequired(false));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(false));
		serverInfoResponse.add(new SupportsIndex(false));
		serverInfoResponse.add(new DatabaseCount(itemManager.getDatabases().size()));

		return Util.buildResponse(serverInfoResponse, itemManager.getDMAPKey(), name);
	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DPAP_VERSION_KEY, DmapUtil.PPRO_VERSION_200 + "");
		records.put(IPSH_VERSION_KEY, DmapUtil.PPRO_VERSION_200 + "");
		records.put(MACHINE_ID_KEY, hash);
		records.put(PASSWORD_KEY, "0");
		return ServiceInfo.create(DPAP_SERVICE_TYPE, name, port, 0, 0, records);
	}

}
