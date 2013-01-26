/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
package org.dyndns.jkiddo.dpap.server;

import java.io.IOException;
import java.util.HashMap;

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
import org.dyndns.jkiddo.dmap.service.MDNSResource;
import org.dyndns.jkiddo.dmap.service.Util;
import org.dyndns.jkiddo.protocol.dmap.DmapUtil;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.DaapProtocolVersion;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.DmapProtocolVersion;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ServerInfoResponse;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.Status;

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

	private static final String DMAP_KEY = "DPAP-Server";

	@Inject
	public ImageResource(JmmDNS mDNS, @Named(DPAP_SERVER_PORT_NAME) Integer port) throws IOException
	{
		super(mDNS, port);
		this.signUp();
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
	public Response serverInfo(@Context UriInfo uri, @Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		// See how it is done in MusicLibraryResource
		// return
		// msrv
		// mstt
		// mpro
		// ppro

		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(new DmapProtocolVersion(DmapUtil.DMAP_VERSION_420));
		serverInfoResponse.add(new DaapProtocolVersion(DmapUtil.DPAP_VERSION_411));

		// minm
		// mslr
		// mstm
		// msal
		// msix
		// msdc

		return Util.buildResponse(serverInfoResponse, DMAP_KEY, "Some name");
	}
	@Override
	@Path("/login")
	@GET
	public Response login(@Context HttpServletRequest httpServletRequest) throws IOException
	{
		// See how it is done in MusicLibraryResource
		// return
		// mlog
		// mstt
		// mlid
		throw new NotImplementedException();
	}

	@Override
	@Path("/update")
	@GET
	public Response update(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @Context HttpServletRequest httpServletRequest) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases")
	@GET
	public Response databases(@Context UriInfo uri, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta) throws IOException
	{
		// See how it is done in MusicLibraryResource
		// return
		// avdb
		// mstt
		// muty
		// mtco
		// mrco
		// mlcl
		// mlit
		// miid
		// minm
		// mimc
		// mctc
		throw new NotImplementedException();
	}
	@Override
	@Path("/databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/items")
	@GET
	public Response items(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta) throws Exception
	{
		// Picture request
		// GET dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.hires,dmap.itemid,dpap.filedata&query=('dmap.itemid:2742') HTTP/1.1
		//
		// 5487
		// HTTP/1.1 200 OK
		// Date: Fri, 18 Jan 2013 18:57:33 GMT
		// DPAP-Server: iPhoto/9.2.1 (Mac OS X)
		// Content-Type: application/x-dmap-tagged
		// Content-Length: 6510908
		//
		// adbscY4msttmutymtcomrcomlclcXmlitcXmikdmiid
		// pfdtcX
		// and the the binary image is added

		// See how it is done in MusicLibraryResource
		// adbsmsttmutymtcomrcomlcltmlitmikdmiidpfdt
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/containers")
	@GET
	public Response containers(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException
	{
		// See how it is done in MusicLibraryResource
		// aplymsttmutymtcomrcomlclmlitLmikdmiidminmSome Name bibliotekabplmimcmlit3mikdmiidminm
		// 03/04/2007mimcmlit4mikdmiidminmLast Importmimcmlit7mikdmiidminmLast 12 Monthsmimcmlit2mikdmiidminm tom mappemimcmlit:mikdmiidminmAlbum uden navn 3mimcmlit4mikdmiid9minmFamiliefestmimcmlit?mikdmiidminmEmner til fremkaldelsemimc0mlit:mikdmiid%minmAlbum uden navn 2mimcmlit0mikdmiidminmBettinamimcmlit1mikdmiidminmKalendermimc1mlit2mikdmiid!minm Fastelavnmimcmlit3mikdmiidminm
		// Kbenhavnmimcmlit0mikdmiidminmFlaggedmimcmlit.mikdmiidminmTrashmimc
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException
	{
		// See how it is done in MusicLibraryResource
		// apso)msttmutymtcomrcomlcl)zmlitmikdpasp1.5picd8m5pmiid
		// pimfIMG_0041.JPGminmIMG_0041.JPGpifsf[pwthphgt pfmtJPEGpratplszf[mlitmikdpasp1.5picd8m5pmiid
		// pimfIMG_0043.JPGminmIMG_0043.JPGpifsuYpwthphgt pfmtJPEGpratplszuYmlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0026.JPGminmIMG_0026.JPGpifsJpwthphgt pfmtJPEGpratplszJmlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0039.JPGminmIMG_0039.JPGpifs]3pwthphgt pfmtJPEGpratplsz]3mlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0047.JPGminmIMG_0047.JPGpifsnpwthphgt pfmtJPEGpratplsznmlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0065.JPGminmIMG_0065.JPGpifs`pwthphgt pfmtJPEGpratplsz`mlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0077.JPGminmIMG_0077.JPGpifs?"pwthphgt pfmtJPEGpratplsz?"mlitmikdpasp1.5picd8m5rmiid
		// pimfIMG_0045.JPGminmIMG_0045.JPGpifs`\pwthphgt pfmtJPEGpratplsz`\mlitmikdpasp1.5picd8m5rm
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/content-codes")
	@GET
	public Response contentCodes() throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/logout")
	@GET
	public Response logout(@Context UriInfo uri, @Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId)
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/resolve")
	@GET
	public Response resolve()
	{
		throw new NotImplementedException();
	}

}
