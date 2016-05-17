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
package org.dyndns.jkiddo.service.dmap;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.UnknownClientTypeException;
import org.dyndns.jkiddo.raop.server.airreceiver.Base64;
import org.dyndns.jkiddo.service.daap.server.IMusicLibrary;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.server.ITouchAbleServerResource;
import org.dyndns.jkiddo.service.dpap.server.IImageLibrary;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;

@Path("/")
@Singleton
public class DMAPInterface implements ITouchAbleServerResource,
		ITouchRemoteResource, IMusicLibrary, IImageLibrary {
	@Context
	private HttpServletRequest httpServletRequest;

	// @Context
	// private HttpServletResponse httpServletResponse;

	// @Context
	// private UriInfo uriInfo;

	private final ITouchAbleServerResource remoteControlResource;
	private final ITouchRemoteResource pairingResource;
	private final IMusicLibrary musicLibraryResource;
	private final IImageLibrary imageLibraryResource;

	private static final String CONTROLLER_HEADER_NAME = "Viewer-Only-Client";
	private static final String CONTROLLER_USER_AGENT = "Remote";
	private static final String DAAP_HEADER_NAME = "Client-DAAP-Version";
	private static final String DPAP_HEADER_NAME = "Client-DPAP-Version";

	@Inject
	public DMAPInterface(final ITouchAbleServerResource remoteControlResource,
			final ITouchRemoteResource pairingResource,
			final IMusicLibrary musicLibraryResource,
			final IImageLibrary imageLibraryResource) {
		this.remoteControlResource = remoteControlResource;
		this.pairingResource = pairingResource;
		this.musicLibraryResource = musicLibraryResource;
		this.imageLibraryResource = imageLibraryResource;
	}

	private static boolean isDaapRequest(
			final HttpServletRequest httpServletRequest) {
		if (Strings.isNullOrEmpty(httpServletRequest
				.getHeader(DAAP_HEADER_NAME)))
			return false;
		return true;
	}

	private static boolean isDpapRequest(
			final HttpServletRequest httpServletRequest) {
		if (Strings.isNullOrEmpty(httpServletRequest
				.getHeader(DPAP_HEADER_NAME)))
			return false;
		return true;
	}

	private static boolean isRemoteControlRequest(
			final HttpServletRequest httpServletRequest) {
		return false;
		/*
		 * if(Strings.isNullOrEmpty(httpServletRequest.getHeader(
		 * CONTROLLER_HEADER_NAME))) return false;
		 * if(!httpServletRequest.getHeader
		 * (HttpHeaders.USER_AGENT).startsWith(CONTROLLER_USER_AGENT)) return
		 * false; return true;
		 */
	}

	@Path("favicon.ico")
	@GET
	public Response favicon() {
		return Response.noContent().build();
	}

	@Override
	@Path("server-info")
	@GET
	public Response serverInfo(@QueryParam("hsgid") final String hsgid)
			throws IOException, SQLException {
		if (isRemoteControlRequest(httpServletRequest))
			return remoteControlResource.serverInfo(hsgid);
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.serverInfo(hsgid);
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.serverInfo(hsgid);
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("login")
	@GET
	public Response login(@QueryParam("pairing-guid") final String guid,
			@QueryParam("hasFP") final int value,
			@QueryParam("hsgid") final String hsgid) throws IOException {
		if (isRemoteControlRequest(httpServletRequest))
			return remoteControlResource.login(guid, value, hsgid);
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.login(guid, value, hsgid);
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.login(guid, value, hsgid);
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("update")
	@GET
	public Response update(@QueryParam("session-id") final long sessionId,
			@QueryParam("revision-number") final long revisionNumber,
			@QueryParam("delta") final long delta,
			@QueryParam("daap-no-disconnect") final int daapNoDisconnect,
			@QueryParam("hsgid") final String hsgid) throws IOException {
		if (isRemoteControlRequest(httpServletRequest))
			// return remoteControlResource.update(httpServletRequest,
			// httpServletResponse, info, sessionId, revisionNumber, delta,
			// daapNoDisconnect);
			return musicLibraryResource.update(sessionId, revisionNumber,
					delta, daapNoDisconnect, hsgid);
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.update(sessionId, revisionNumber,
					delta, daapNoDisconnect, hsgid);
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.update(sessionId, revisionNumber,
					delta, daapNoDisconnect, hsgid);
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("databases")
	@GET
	public Response databases(@QueryParam("session-id") final long sessionId,
			@QueryParam("revision-number") final long revisionNumber,
			@QueryParam("delta") final long delta,
			@QueryParam("hsgid") final String hsgid) throws IOException,
			SQLException {
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.databases(sessionId, revisionNumber,
					delta, hsgid);
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.databases(sessionId, revisionNumber,
					delta, hsgid);
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("databases/{databaseId}/items")
	@GET
	public Response items(@PathParam("databaseId") final long databaseId,
			@QueryParam("session-id") final long sessionId,
			@QueryParam("revision-number") final long revisionNumber,
			@QueryParam("delta") final long delta,
			@QueryParam("type") final String type,
			@QueryParam("meta") final String meta,
			@QueryParam("query") final String query,
			@QueryParam("hsgid") final String hsgid) throws IOException,
			SQLException {
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.items(databaseId, sessionId,
					revisionNumber, delta, type, meta, query, hsgid);
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.items(databaseId, sessionId,
					revisionNumber, delta, type, meta, query, hsgid);
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("databases/{databaseId}/containers")
	@GET
	public Response containers(@PathParam("databaseId") final long databaseId,
			@QueryParam("session-id") final long sessionId,
			@QueryParam("revision-number") final long revisionNumber,
			@QueryParam("delta") final long delta,
			@QueryParam("meta") final String meta,
			@QueryParam("hsgid") final String hsgid) throws IOException,
			SQLException {
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.containers(databaseId, sessionId,
					revisionNumber, delta, meta, hsgid);
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.containers(databaseId, sessionId,
					revisionNumber, delta, meta, hsgid);
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(
			@PathParam("containerId") final long containerId,
			@PathParam("databaseId") final long databaseId,
			@QueryParam("session-id") final long sessionId,
			@QueryParam("revision-number") final long revisionNumber,
			@QueryParam("delta") final long delta,
			@QueryParam("meta") final String meta,
			@QueryParam("type") final String type,
			@QueryParam("group-type") final String group_type,
			@QueryParam("sort") final String sort,
			@QueryParam("include-sort-headers") final String include_sort_headers,
			@QueryParam("query") final String query,
			@QueryParam("index") final String index,
			@QueryParam("hsgid") final String hsgid) throws IOException,
			SQLException {
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.containerItems(containerId, databaseId,
					sessionId, revisionNumber, delta, meta, type, group_type,
					sort, include_sort_headers, query, index, hsgid);
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.containerItems(containerId, databaseId,
					sessionId, revisionNumber, delta, meta, type, group_type,
					sort, include_sort_headers, query, index, hsgid);
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("content-codes")
	@GET
	public Response contentCodes() throws IOException {
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.contentCodes();
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.contentCodes();
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@PathParam("databaseId") final long databaseId,
			@PathParam("itemId") final long itemId,
			@PathParam("format") final String format,
			@HeaderParam("Range") final String rangeHeader) throws IOException {
		return musicLibraryResource.item(databaseId, itemId, format,
				rangeHeader);
	}

	@Override
	@GET
	@Path("pair")
	public Response pair(@Context final HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse,
			@QueryParam("pairingcode") final String pairingcode,
			@QueryParam("servicename") final String servicename)
			throws IOException {
		return pairingResource.pair(httpServletRequest, httpServletResponse,
				pairingcode, servicename);
	}

	@Override
	@GET
	@Path("logout")
	public Response logout(@QueryParam("session-id") final long sessionId) {
		if (isRemoteControlRequest(httpServletRequest))
			return remoteControlResource.logout(sessionId);
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.logout(sessionId);
		if (isDpapRequest(httpServletRequest))
			return imageLibraryResource.logout(sessionId);
		throw new UnknownClientTypeException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/pause")
	public Response pause(@QueryParam("session-id") final long session_id) {
		return remoteControlResource.pause(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/stop")
	public Response stop(@QueryParam("session-id") final long session_id) {
		return remoteControlResource.stop(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playpause")
	public Response playpause(@QueryParam("session-id") final long session_id) {
		return remoteControlResource.playpause(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/nextitem")
	public Response nextitem(@QueryParam("session-id") final long session_id) {
		return remoteControlResource.nextitem(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/previtem")
	public Response previtem(@QueryParam("session-id") final long session_id) {
		return remoteControlResource.previtem(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playlist")
	public Response playlist(@QueryParam("session-id") final long session_id) {
		return remoteControlResource.playlist(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playstatusupdate")
	public Response playstatusupdate(
			@QueryParam("revision-number") final long revisionNumber,
			@QueryParam("session-id") final long session_id,
			@QueryParam("hsgid") final String hsgid) throws IOException {
		return remoteControlResource.playstatusupdate(revisionNumber,
				session_id, hsgid);
	}

	@Override
	@GET
	@Path("ctrl-int/1/getspeakers")
	public Response getspeakers(
			@QueryParam("session-id") final long session_id,
			@QueryParam("hsgid") final String hsgid) throws IOException {
		return remoteControlResource.getspeakers(session_id, hsgid);
	}

	@Override
	@GET
	@Path("ctrl-int/1/setspeakers")
	public Response setspeakers(
			@QueryParam("speaker-id") final String speaker_id,
			@QueryParam("session-id") final long session_id) {
		return remoteControlResource.setspeakers(speaker_id, session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playspec")
	public Response playspec(
			@QueryParam("container-item-spec") final String container_item_spec,
			@QueryParam("item-spec") final String item_spec,
			@QueryParam("container-spec") final String container_spec,
			@QueryParam("dacp.shufflestate") final String dacp_shufflestate,
			@QueryParam("database-spec") final String database_spec,
			@QueryParam("playlist-spec") final String playlist_spec,
			@QueryParam("session-id") final long session_id) {
		return remoteControlResource.playspec(container_item_spec, item_spec,
				container_spec, dacp_shufflestate, database_spec,
				playlist_spec, session_id);
	}

	@Override
	@Path("databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") final long databaseId,
			@QueryParam("meta") final String meta,
			@QueryParam("type") final String type,
			@QueryParam("group-type") final String groupType,
			@QueryParam("sort") final String sort,
			@QueryParam("include-sort-headers") final long includeSortHeaders,
			@QueryParam("query") final String query,
			@QueryParam("session-id") final long sessionId,
			@QueryParam("hsgid") final String hsgid) throws IOException,
			SQLException {
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.groups(databaseId, meta, type,
					groupType, sort, includeSortHeaders, query, sessionId,
					hsgid);
		throw new UnknownClientTypeException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/setproperty")
	public Response setproperty(@Context final UriInfo uriInfo,
			@QueryParam("dmcp.volume") final String dmcpVolume,
			@QueryParam("dacp.playingtime") final String dacpPlayingtime,
			@QueryParam("dacp.shufflestate") final String dacpShufflestate,
			@QueryParam("dacp.repeatstate") final String dacpRepeatstate,
			@QueryParam("session-id") final long session_id) {
		return remoteControlResource.setproperty(uriInfo, dmcpVolume,
				dacpPlayingtime, dacpShufflestate, dacpRepeatstate, session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/getproperty")
	public Response getproperty(@Context final UriInfo uriInfo,
			@QueryParam("properties") final String properties,
			@QueryParam("session-id") final long session_id,
			@QueryParam("hsgid") final String hsgid) throws IOException {
		return remoteControlResource.getproperty(uriInfo, properties,
				session_id, hsgid);
	}

	@Override
	@GET
	@Path("ctrl-int/1/cue")
	public Response cue(@QueryParam("commmand") final String command,
			@QueryParam("query") final String query,
			@QueryParam("index") final String index,
			@QueryParam("sort") final String sort,
			@QueryParam("session-id") final long session_id) {
		return remoteControlResource.cue(command, query, index, sort,
				session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/nowplayingartwork")
	public Response nowplayingartwork(@QueryParam("mw") final String mw,
			@QueryParam("mh") final String mh,
			@QueryParam("session-id") final long session_id,
			@QueryParam("hsgid") final String hsgid) {
		return remoteControlResource.nowplayingartwork(mw, mh, session_id,
				hsgid);
	}

	@Override
	@GET
	@Path("ctrl-int/1/set-genius-seed")
	public Response editGenius(
			@QueryParam("database-spec") final String database_spec,
			@QueryParam("item-spec") final String item_spec,
			@QueryParam("session-id") final long session_id) {
		return remoteControlResource.editGenius(database_spec, item_spec,
				session_id);
	}

	@Override
	@Path("databases/{databaseId}/edit")
	@GET
	public Response editPlaylist(
			@PathParam("databaseId") final long databaseId,
			@QueryParam("session-id") final long sessionId,
			@QueryParam("action") final String action,
			@QueryParam("edit-params") final String edit_params)
			throws IOException {
		return remoteControlResource.editPlaylist(databaseId, sessionId,
				action, edit_params);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playqueue-contents")
	public Response playQueueContents(@QueryParam("span") final int span,
			@QueryParam("session-id") final long session_id) {
		return remoteControlResource.playQueueContents(span, session_id);
	}

	@Override
	@POST
	@Consumes("application/octet-stream")
	@Path("fp-setup")
	public Response fpSetup(@QueryParam("session-id") final long session_id,
			@QueryParam("hsgid") final String hsgid,
			final InputStream inputStream) throws IOException {
		final byte[] bytes = ByteStreams.toByteArray(inputStream);
		System.out.println(Base64.encodePadded(bytes));
		 final byte[] firstResponse = new
		 byte[]{0x1f,(byte)0x8b,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xff,(byte)0x01,(byte)0x8e,(byte)0x00,(byte)0x71,(byte)0xff,(byte)0x46,(byte)0x50,(byte)0x4c,(byte)0x59,(byte)0x02,(byte)0x01,(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x82,(byte)0x02,(byte)0x00,(byte)0x39,(byte)0x71,(byte)0x80,(byte)0xa9,(byte)0x21,(byte)0xfa,(byte)0xdd,(byte)0xbb,(byte)0x32,(byte)0xdc,(byte)0x3f,(byte)0x50,(byte)0x6f,(byte)0x10,(byte)0xc5,(byte)0x28,(byte)0x77,(byte)0x90,(byte)0xbf,(byte)0xb5,(byte)0x87,(byte)0x34,(byte)0x0c,(byte)0xf1,(byte)0xe8,(byte)0xdd,(byte)0x80,(byte)0x88,(byte)0x8b,(byte)0x68,(byte)0xbc,(byte)0x35,(byte)0x0c,(byte)0x58,(byte)0x69,(byte)0x96,(byte)0x9d,(byte)0x2e,(byte)0x31,(byte)0x9f,(byte)0x35,(byte)0x46,(byte)0x19,(byte)0x97,(byte)0x9b,(byte)0x22,(byte)0x65,(byte)0xdf,(byte)0x06,(byte)0xc7,(byte)0x46,(byte)0x23,(byte)0x47,(byte)0xa1,(byte)0x12,(byte)0x48,(byte)0x4a,(byte)0xe0,(byte)0xa4,(byte)0x30,(byte)0x02,(byte)0xba,(byte)0xfa,(byte)0xaf,(byte)0x10,(byte)0xa2,(byte)0x2b,(byte)0xa1,(byte)0x0f,(byte)0xb6,(byte)0xc3,(byte)0x54,(byte)0x6d,(byte)0x99,(byte)0x9b,(byte)0xa1,(byte)0xf0,(byte)0xe2,(byte)0x05,(byte)0xa6,(byte)0xf1,(byte)0xab,(byte)0xc8,(byte)0x6c,(byte)0x70,(byte)0xb6,(byte)0x7c,(byte)0xbb,(byte)0xff,(byte)0x83,(byte)0xa3,(byte)0xd0,(byte)0xa1,(byte)0x68,(byte)0x06,(byte)0x48,(byte)0x9e,(byte)0x49,(byte)0xf4,(byte)0x69,(byte)0xac,(byte)0x8c,(byte)0x3b,(byte)0xad,(byte)0xc2,(byte)0xe7,(byte)0x5a,(byte)0xde,(byte)0x52,(byte)0x9b,(byte)0x01,(byte)0x60,(byte)0x1a,(byte)0xcb,(byte)0x46,(byte)0xc9,(byte)0x48,(byte)0xcb,(byte)0x80,(byte)0xaf,(byte)0x59,(byte)0x1f,(byte)0x1b,(byte)0xfc,(byte)0x0c,(byte)0x56,(byte)0xe0,(byte)0x55,(byte)0xfd,(byte)0xd0,(byte)0x61,(byte)0x0b,(byte)0x8e,(byte)0x00,(byte)0x00,(byte)0x00};
		 final byte[] secondResponse = new
		 byte[]{(byte)0x1f,(byte)0x8b,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xff,(byte)0x73,(byte)0x0b,(byte)0xf0,(byte)0x89,(byte)0x64,(byte)0x62,(byte)0x64,(byte)0x61,(byte)0x00,(byte)0x02,(byte)0x91,(byte)0xa2,(byte)0x23,(byte)0x3f,(byte)0xff,(byte)0xda,(byte)0xd7,(byte)0x09,(byte)0x2e,(byte)0xcf,(byte)0x72,(byte)0x9e,(byte)0x94,(byte)0x5d,(byte)0xe4,(byte)0x7f,(byte)0xea,(byte)0xbd,(byte)0x5a,(byte)0x6b,(byte)0x77,(byte)0x01,(byte)0x00,(byte)0xb7,(byte)0x30,(byte)0xf1,(byte)0x65,(byte)0x20,(byte)0x00,(byte)0x00,(byte)0x00};

/*		final Socket clientSocket = new Socket("192.168.1.78", 19999);
		final DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());

		byte challenge;
		if (bytes.length == 16)
			challenge = 1;
		else
			challenge = 2;
		
		final byte[] fullChallenge = Bytes.concat(new byte[] { challenge, (byte) (bytes.length + 2)}, bytes);

		System.out.println(Base64.encodePadded(fullChallenge));
		
		outToServer.write(fullChallenge);
		outToServer.flush();

	    byte[] resultBuff = new byte[0];
	    final byte[] buff = new byte[1024];
	    int k = -1;
	    while((k = clientSocket.getInputStream().read(buff, 0, buff.length)) > -1) {
	        final byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
	        System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
	        System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
	        resultBuff = tbuff; // call the temp buffer as your result buff
	    }

		clientSocket.close();
		
		System.out.println(Base64.encodePadded(resultBuff));*/
		 byte[] resultBuff ;
		 if (bytes.length == 16)
			 resultBuff = firstResponse; 
			else
				resultBuff = secondResponse;

		
		return Util.buildBinaryResponse(resultBuff, "DAAP-Server",
				"iTunes/12.3.0.44 (OS X)");
	}

	@Override
	@GET
	@Path("ctrl-int")
	public Response ctrlInt(
			@Context final HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse,
			@QueryParam("hsgid") final String hsgid) throws IOException {
		return remoteControlResource.ctrlInt(httpServletRequest,
				httpServletResponse, hsgid);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playqueue-edit")
	public Response playQueueEdit(
			@Context final HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse,
			@QueryParam("commmand") final String command,
			@QueryParam("query") final String query,
			@QueryParam("queuefilter") final String index,
			@QueryParam("sort") final String sort,
			@QueryParam("session-id") final long session_id) throws Exception {
		return remoteControlResource.playQueueEdit(httpServletRequest,
				httpServletResponse, command, query, index, sort, session_id);
	}

	@Override
	// @Path("databases/{databaseId}/groups/{groupdId}/extra_data/artwork")
	@Path("databases/{databaseId}/items/{groupdId}/extra_data/artwork")
	@GET
	public Response artwork(@PathParam("databaseId") final long databaseId,
			@PathParam("groupId") final long groupId,
			@QueryParam("session-id") final long sessionId,
			@QueryParam("mw") final String mw,
			@QueryParam("mh") final String mh,
			@QueryParam("group-type") final String group_type,
			@QueryParam("daapSecInfo") final String daapSecInfo)
			throws IOException {
		if (isDaapRequest(httpServletRequest))
			return musicLibraryResource.artwork(databaseId, groupId, sessionId,
					mw, mh, group_type, daapSecInfo);
		throw new UnknownClientTypeException();
	}

	@Override
	@Path("this_request_is_simply_to_send_a_close_connection_header")
	@GET
	public Response closeConnection() throws IOException {
		return imageLibraryResource.closeConnection();
	}

	@Override
	@GET
	@Path("home-share-verify")
	public Response homeShareVerify(
			@QueryParam("session-id") final long session_id,
			@QueryParam("hsgid") final String hsgid,
			@QueryParam("hspid") final String hspid) {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Path("resolve")
	public Response resolve() throws IOException {
		return null;
	}

	@Override
	public Response artwork(final long databaseId, final String mw,
			final String mh, final long sessionId, final String groupType,
			final String hsgid) {
		return null;
	}

	@Override
	public Response artists(final long databaseId, final long sessionId,
			final long includeSortHeaders, final String filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
