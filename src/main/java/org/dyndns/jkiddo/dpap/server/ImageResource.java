package org.dyndns.jkiddo.dpap.server;

import java.io.IOException;
import java.util.HashMap;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.dmap.service.MDNSResource;
import org.dyndns.jkiddo.protocol.dmap.DmapUtil;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ImageResource extends MDNSResource implements IImageLibrary
{
	public static final String DPAP_SERVER_PORT_NAME = "DPAP_SERVER_PORT_NAME";

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DPAP_VERSION_KEY = "Version";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String IPSH_VERSION_KEY = "iPSh Version";
	private static final String PASSWORD_KEY = "Password";
	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";

	@Inject
	public ImageResource(JmmDNS mDNS, @Named(DPAP_SERVER_PORT_NAME) Integer port) throws IOException
	{
		super(mDNS, port);
	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DPAP_VERSION_KEY, DmapUtil.DPAP_VERSION_1 + "");
		records.put(IPSH_VERSION_KEY, 0x20000 + "");
		records.put(MACHINE_ID_KEY, hash);
		records.put(PASSWORD_KEY, "0");
		return ServiceInfo.create(DPAP_SERVICE_TYPE, Jolivia.name, port, 0, 0, records);
	}

	@Override
	@Path("/server-info")
	@GET
	public Response serverInfo() throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/login")
	@GET
	public Response login(@Context HttpServletRequest httpServletRequest) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/update")
	@GET
	public Response update(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @Context HttpServletRequest httpServletRequest) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/databases")
	@GET
	public Response databases(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/databases/{databaseId}/items")
	@GET
	public Response items(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/databases/{databaseId}/containers")
	@GET
	public Response containers(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/content-codes")
	@GET
	public Response contentCodes() throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/logout")
	@GET
	public Response logout(@QueryParam("session-id") long sessionId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("/resolve")
	@GET
	public Response resolve()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
