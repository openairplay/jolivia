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
package org.dyndns.jkiddo.service.dacp.server;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface ITouchAbleServerResource
{
	public final static String DACP_TYPE = "_dacp._tcp.local.";
	/**
	 * Service running when the iTunes instance is already paired with some remotes.
	 */
	public final static String TOUCH_ABLE_SERVER = "_touch-able._tcp.local.";

	@GET
	@Path("server-info")
	public Response serverInfo(@QueryParam("hsgid") String hsgid) throws IOException, SQLException;
	
	@GET
	@Path("login")
	public Response login(@QueryParam("pairing-guid") String guid, @QueryParam("hasFP") int value, @QueryParam("hsgid") String hsgid) throws IOException;

	@Path("update")
	@GET
	public Response update(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("daap-no-disconnect") int daapNoDisconnect, @QueryParam("hsgid") String hsgid) throws IOException;

	@GET
	@Path("logout")
	public Response logout(@QueryParam("session-id") long session_id);

	@GET
	@Path("fp-setup")
	public Response fpSetup(@QueryParam("session-id") long session_id, @QueryParam("hsgid") String hsgid);
	
	@GET
	@Path("home-share-verify")
	public Response homeShareVerify(@QueryParam("session-id") long session_id, @QueryParam("hsgid") String hsgid, @QueryParam("hspid") String hspid);

	@Path("databases")
	@GET
	public Response databases(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("hsgid") String hsgid) throws IOException, SQLException;
	
	@Path("databases/{databaseId}/edit")
	@GET
	public Response editPlaylist(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("action") String action, @QueryParam("edit-params") String edit_params) throws IOException;
	
	@Path("databases/{databaseId}/containers")
	@GET
	public Response containers(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("hsgid") String hsgid) throws IOException, SQLException;
	
	@Path("databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") long databaseId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String groupType, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") long includeSortHeaders, @QueryParam("query") String query, @QueryParam("session-id") long sessionId, @QueryParam("hsgid") String hsgid) throws IOException;
	
	@Path("databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index, @QueryParam("hsgid") String hsgid) throws IOException, SQLException;

	@GET
	@Path("ctrl-int")
	public Response ctrlInt(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("hsgid") String hsgid) throws IOException;

	@GET
	@Path("ctrl-int/1/pause")
	public Response pause(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/stop")
	public Response stop(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playpause")
	public Response playpause(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/nextitem")
	public Response nextitem(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/previtem")
	public Response previtem(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playlist")
	public Response playlist(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/setproperty")
	public Response setproperty(@Context UriInfo uriInfo, @QueryParam("dmcp.volume") String dmcpVolume, @QueryParam("dacp.playingtime") String dacpPlayingtime, @QueryParam("dacp.shufflestate") String dacpShufflestate, @QueryParam("dacp.repeatstate") String dacpRepeatstate, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/getproperty")
	public Response getproperty(@Context UriInfo uriInfo, @QueryParam("properties") String properties, @QueryParam("session-id") long session_id, @QueryParam("hsgid") String hsgid) throws IOException;

	@GET
	@Path("ctrl-int/1/playstatusupdate")
	public Response playstatusupdate(@QueryParam("revision-number") long revisionNumber, @QueryParam("session-id") long session_id, @QueryParam("hsgid") String hsgid) throws IOException;

	@GET
	@Path("ctrl-int/1/cue")
	public Response cue(@QueryParam("commmand") String command, @QueryParam("query") String query, @QueryParam("index") String index, @QueryParam("sort") String sort, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/getspeakers")
	public Response getspeakers(@QueryParam("session-id") long session_id, @QueryParam("hsgid") String hsgid) throws IOException;

	@GET
	@Path("ctrl-int/1/setspeakers")
	public Response setspeakers(@QueryParam("speaker-id") String speaker_id, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playspec")
	public Response playspec(@QueryParam("container-item-spec") String container_item_spec, @QueryParam("item-spec") String item_spec, @QueryParam("container-spec") String container_spec, @QueryParam("dacp.shufflestate") String dacp_shufflestate, @QueryParam("database-spec") String database_spec, @QueryParam("playlist-spec") String playlist_spec, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playqueue-contents")
	public Response playQueueContents(@QueryParam("span") int span, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/nowplayingartwork")
	public Response nowplayingartwork(@QueryParam("mw") String mw, @QueryParam("mh") String mh, @QueryParam("session-id") long session_id, @QueryParam("hsgid") String hsgid);

	@GET
	@Path("ctrl-int/1/set-genius-seed")
	public Response editGenius(@QueryParam("database-spec") String database_spec, @QueryParam("item-spec") String item_spec, @QueryParam("session-id") long session_id);
	
	@Path("ctrl-int/1/playqueue-edit")
	@GET
	public Response playQueueEdit(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("commmand") String command, @QueryParam("query") String query, @QueryParam("queuefilter") String index, @QueryParam("sort") String sort, @QueryParam("session-id") long session_id) throws Exception;
}
