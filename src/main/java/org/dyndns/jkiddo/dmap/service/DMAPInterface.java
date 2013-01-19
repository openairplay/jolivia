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
package org.dyndns.jkiddo.dmap.service;

import java.io.IOException;

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

import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.daap.server.IMusicLibrary;
import org.dyndns.jkiddo.dacp.client.IPairingResource;
import org.dyndns.jkiddo.dacp.server.IRemoteControlResource;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Path("/")
@Singleton
public class DMAPInterface implements IRemoteControlResource, IPairingResource, IMusicLibrary
{
	IRemoteControlResource remoteControlResource;
	IPairingResource pairingResource;
	IMusicLibrary musicLibraryResource;

	@Inject
	public DMAPInterface(IRemoteControlResource remoteControlResource, IPairingResource pairingResource, IMusicLibrary musicLibraryResource)
	{
		this.remoteControlResource = remoteControlResource;
		this.pairingResource = pairingResource;
		this.musicLibraryResource = musicLibraryResource;
	}

	@Override
	@Path("/server-info")
	@GET
	public Response serverInfo() throws IOException
	{
		return musicLibraryResource.serverInfo();
	}

	@Override
	public Response login(@Context HttpServletRequest httpServletRequest) throws IOException
	{
		return musicLibraryResource.login(httpServletRequest);
	}

	@Override
	@Path("/login")
	@GET
	public Response login(@Context HttpServletRequest httpServletRequest, @QueryParam("pairing-guid") String guid) throws IOException
	{
		if(Strings.isNullOrEmpty(guid))
			return login(httpServletRequest);
		// TODO: Don't know what else to do as of now
		return login(httpServletRequest);
	}

	@Override
	@Path("/update")
	@GET
	public Response update(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @Context HttpServletRequest httpServletRequest) throws IOException
	{
		return musicLibraryResource.update(sessionId, revisionNumber, delta, httpServletRequest);
	}

	@Override
	@Path("/databases")
	@GET
	public Response databases(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta) throws IOException
	{
		return musicLibraryResource.databases(sessionId, revisionNumber, delta);
	}

	@Override
	@Path("/databases/{databaseId}/items")
	@GET
	public Response items(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta) throws Exception
	{
		return musicLibraryResource.items(databaseId, sessionId, revisionNumber, delta, type, meta);
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}/extra_data/artwork")
	@GET
	public Response artwork(@PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("mw") String mw, @QueryParam("mh") String mh) throws Exception
	{
		return musicLibraryResource.artwork(databaseId, itemId, sessionId, revisionNumber, mw, mh);
	}

	@Override
	@Path("/databases/{databaseId}/containers")
	@GET
	public Response containers(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException
	{
		return musicLibraryResource.containers(databaseId, sessionId, revisionNumber, delta, meta);
	}

	@Override
	@Path("/databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException
	{
		return musicLibraryResource.containerItems(containerId, databaseId, sessionId, revisionNumber, delta, meta, type, group_type, sort, include_sort_headers, query, index);
	}

	@Override
	@Path("/content-codes")
	@GET
	public Response contentCodes() throws IOException
	{
		return musicLibraryResource.contentCodes();
	}

	// @Override
	// @Path("/logout")
	// @GET
	// public Response logout()
	// {
	// return libraryResource.logout();
	// }

	@Override
	@Path("/resolve")
	@GET
	public Response resolve()
	{
		return musicLibraryResource.resolve();
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception
	{
		return musicLibraryResource.item(databaseId, itemId, format, rangeHeader);
	}

	@Override
	@GET
	@Path("pair")
	public Response pair(@Context UriInfo uri, @Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("pairingcode") String pairingcode, @QueryParam("servicename") String servicename) throws IOException
	{
		return pairingResource.pair(uri, httpServletRequest, httpServletResponse, pairingcode, servicename);
	}

	// @Override
	// public Response login(@QueryParam("pairing-guid") String guid)
	// {
	// return remoteControlResource.login(guid);
	// }

	@Override
	@GET
	@Path("logout")
	public Response logout(@QueryParam("session-id") long sessionId)
	{
		Response musicLibraryResponse = musicLibraryResource.logout(sessionId);
		Response remoteControlResponse = remoteControlResource.logout(sessionId);
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/pause")
	public String pause(@QueryParam("session-id") long session_id)
	{
		return remoteControlResource.pause(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/stop")
	public String stop(@QueryParam("session-id") long session_id)
	{
		return remoteControlResource.stop(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playpause")
	public String playpause(@QueryParam("session-id") long session_id)
	{
		return remoteControlResource.playpause(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/nextitem")
	public String nextitem(@QueryParam("session-id") long session_id)
	{
		return remoteControlResource.nextitem(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/previtem")
	public String previtem(@QueryParam("session-id") long session_id)
	{
		return remoteControlResource.previtem(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playlist")
	public String playlist(@QueryParam("session-id") long session_id)
	{
		return remoteControlResource.playlist(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playstatusupdate")
	public String playstatusupdate(@QueryParam("revision-number") long revisionNumber, @QueryParam("session-id") long session_id)
	{
		return remoteControlResource.playstatusupdate(revisionNumber, session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/getspeakers")
	public String getspeakers(@QueryParam("session-id") long session_id)
	{
		return remoteControlResource.getspeakers(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/setspeakers")
	public String setspeakers(@QueryParam("speaker-id") String speaker_id, @QueryParam("session-id") long session_id)
	{
		return remoteControlResource.setspeakers(speaker_id, session_id);
	}

	@Override
	@GET
	@Path("/ctrl-int/1/playspec")
	public String playspec(@QueryParam("container-item-spec") String container_item_spec, @QueryParam("item-spec") String item_spec, @QueryParam("container-spec") String container_spec, @QueryParam("dacp.shufflestate") String dacp_shufflestate, @QueryParam("database-spec") String database_spec, @QueryParam("playlist-spec") String playlist_spec, @QueryParam("session-id") long session_id)
	{
		return remoteControlResource.playspec(container_item_spec, item_spec, container_spec, dacp_shufflestate, database_spec, playlist_spec, session_id);
	}

	@Override
	@Path("/databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws Exception
	{
		return musicLibraryResource.groups(databaseId, sessionId, meta, type, group_type, sort, include_sort_headers);
	}

	@Override
	@GET
	@Path("ctrl-int/1/setproperty")
	public String setproperty(@Context UriInfo uriInfo, @QueryParam("dmcp.volume") String dmcpVolume, @QueryParam("dacp.playingtime") String dacpPlayingtime, @QueryParam("dacp.shufflestate") String dacpShufflestate, @QueryParam("dacp.repeatstate") String dacpRepeatstate, @QueryParam("session-id") long session_id)
	{
		return remoteControlResource.setproperty(uriInfo, dmcpVolume, dacpPlayingtime, dacpShufflestate, dacpRepeatstate, session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/getproperty")
	public String getproperty(@Context UriInfo uriInfo, @QueryParam("properties") String properties, @QueryParam("session-id") long session_id)
	{
		return remoteControlResource.getproperty(uriInfo, properties, session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/cue")
	public String cue(@QueryParam("commmand") String command, @QueryParam("query") String query, @QueryParam("index") String index, @QueryParam("sort") String sort, @QueryParam("session-id") long session_id)
	{
		return remoteControlResource.cue(command, query, index, sort, session_id);
	}

	@Override
	@GET
	@Path("/ctrl-int/1/nowplayingartwork")
	public String nowplayingartwork(@QueryParam("mw") String mw, @QueryParam("mh") String mh, @QueryParam("session-id") long session_id)
	{
		return nowplayingartwork(mw, mh, session_id);
	}

	@Override
	@GET
	@Path("/ctrl-int/1/set-genius-seed")
	public String editGenius(@QueryParam("database-spec") String database_spec, @QueryParam("item-spec") String item_spec, @QueryParam("session-id") long session_id)
	{
		return remoteControlResource.editGenius(database_spec, item_spec, session_id);
	}

	@Override
	@Path("/databases/{databaseId}/edit")
	@GET
	public String editPlaylist(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("action") String action, @QueryParam("edit-params") String edit_params) throws IOException
	{
		return remoteControlResource.editPlaylist(databaseId, sessionId, action, edit_params);
	}
}
