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
package org.dyndns.jkiddo.dacp.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface IRemoteControlResource
{
	public final static String DACP_TYPE = "_dacp._tcp.local.";
	public final static String TOUCH_ABLE_TYPE = "_touch-able._tcp.local.";

	@GET
	@Path("login")
	public Response login(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("pairing-guid") String guid, @QueryParam("hasFP") int value) throws IOException;

	@GET
	@Path("logout")
	public Response logout(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/pause")
	public String pause(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/stop")
	public String stop(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playpause")
	public String playpause(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/nextitem")
	public String nextitem(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/previtem")
	public String previtem(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playlist")
	public String playlist(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/setproperty")
	public String setproperty(@Context UriInfo uriInfo, @QueryParam("dmcp.volume") String dmcpVolume, @QueryParam("dacp.playingtime") String dacpPlayingtime, @QueryParam("dacp.shufflestate") String dacpShufflestate, @QueryParam("dacp.repeatstate") String dacpRepeatstate, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/getproperty")
	public String getproperty(@Context UriInfo uriInfo, @QueryParam("properties") String properties, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playstatusupdate")
	public String playstatusupdate(@QueryParam("revision-number") long revisionNumber, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/cue")
	public String cue(@QueryParam("commmand") String command, @QueryParam("query") String query, @QueryParam("index") String index, @QueryParam("sort") String sort, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/getspeakers")
	public String getspeakers(@QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/setspeakers")
	public String setspeakers(@QueryParam("speaker-id") String speaker_id, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playspec")
	public String playspec(@QueryParam("container-item-spec") String container_item_spec, @QueryParam("item-spec") String item_spec, @QueryParam("container-spec") String container_spec, @QueryParam("dacp.shufflestate") String dacp_shufflestate, @QueryParam("database-spec") String database_spec, @QueryParam("playlist-spec") String playlist_spec, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/playqueue-contents")
	public String playQueueContents(@QueryParam("span") int span, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/nowplayingartwork")
	public String nowplayingartwork(@QueryParam("mw") String mw, @QueryParam("mh") String mh, @QueryParam("session-id") long session_id);

	@GET
	@Path("ctrl-int/1/set-genius-seed")
	public String editGenius(@QueryParam("database-spec") String database_spec, @QueryParam("item-spec") String item_spec, @QueryParam("session-id") long session_id);

	@GET
	@Path("fp-setup")
	public String fpSetup(@QueryParam("session-id") long session_id);

	@Path("databases/{databaseId}/edit")
	@GET
	public String editPlaylist(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("action") String action, @QueryParam("edit-params") String edit_params) throws IOException;

	@Path("update")
	@GET
	public Response update(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("daap-no-disconnect") int daapNoDisconnect) throws IOException;
}
