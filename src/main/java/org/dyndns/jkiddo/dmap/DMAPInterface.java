package org.dyndns.jkiddo.dmap;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.daap.server.ILibraryResource;
import org.dyndns.jkiddo.dacp.client.IPairingResource;
import org.dyndns.jkiddo.dacp.server.IRemoteControlResource;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Path("/")
@Singleton
public class DMAPInterface implements IRemoteControlResource, IPairingResource, ILibraryResource
{
	@Inject
	public DMAPInterface()
	{
		// TODO Auto-generated constructor stub
	}
	
	@Inject
	IRemoteControlResource remoteControlResource;
	@Inject
	IPairingResource pairingResource;
	@Inject
	ILibraryResource libraryResource;

	@Override
	@Path("/server-info")
	@GET
	public Response serverInfo() throws IOException
	{
		return libraryResource.serverInfo();
	}

	@Override
	@Path("/login")
	@GET
	public Response login(@Context HttpServletRequest httpServletRequest) throws IOException
	{
		return libraryResource.login(httpServletRequest);
	}

	@Override
	@Path("/update")
	@GET
	public Response update(@QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta, @Context HttpServletRequest httpServletRequest) throws IOException
	{
		return libraryResource.update(sessionId, revisionNumber, delta, httpServletRequest);
	}

	@Override
	@Path("/databases")
	@GET
	public Response databases(@QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta) throws IOException
	{
		return libraryResource.databases(sessionId, revisionNumber, delta);
	}

	@Override
	@Path("/databases/{databaseId}/items")
	@GET
	public Response songs(@PathParam("databaseId") String databaseId, @QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta, @QueryParam("meta") String meta) throws Exception
	{
		return libraryResource.songs(databaseId, sessionId, revisionNumber, delta, meta);
	}

	@Override
	@Path("/databases/{databaseId}/containers")
	@GET
	public Response playlists(@PathParam("databaseId") String databaseId, @QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta, @QueryParam("meta") String meta) throws IOException
	{
		return libraryResource.playlists(databaseId, sessionId, revisionNumber, delta, meta);
	}

	@Override
	@Path("/databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response playlistSongs(@PathParam("containerId") String containerId, @PathParam("databaseId") String databaseId, @QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta, @QueryParam("meta") String meta) throws IOException
	{
		return libraryResource.playlistSongs(containerId, databaseId, sessionId, revisionNumber, delta, meta);
	}

	@Override
	@Path("/content-codes")
	@GET
	public Response contentCodes() throws IOException
	{
		return libraryResource.contentCodes();
	}

	@Override
	@Path("/logout")
	@GET
	public Response logout()
	{
		return libraryResource.logout();
	}

	@Override
	@Path("/resolve")
	@GET
	public Response resolve()
	{
		return libraryResource.resolve();
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response song(@PathParam("databaseId") String databaseId, @PathParam("itemId") String itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception
	{
		return libraryResource.song(databaseId, itemId, format, rangeHeader);
	}

	@Override
	@GET
	@Path("pair")
	public Response pair(@QueryParam("pairingcode") String pairingcode, @QueryParam("servicename") String servicename)
	{
		return pairingResource.pair(pairingcode, servicename);
	}

	@Override
	@GET
	@Path("login")
	public Response login(@QueryParam("pairing-guid") String guid)
	{
		return remoteControlResource.login(guid);
	}

	@Override
	@GET
	@Path("logout")
	public String logout(@QueryParam("session-id") String sessionId)
	{
		return remoteControlResource.logout(sessionId);
	}

	// @Override
	// @GET
	// @Path("databases/{databaseid}/containers")
	// String databases(@PathParam("databaseid") String databaseid, @QueryParam("session-id") String session_id)
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	@GET
	@Path("ctrl-int/1/pause")
	public String pause(@QueryParam("session-id") String session_id)
	{
		return remoteControlResource.pause(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/stop")
	public String stop(@QueryParam("session-id") String session_id)
	{
		return remoteControlResource.stop(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playpause")
	public String playpause(@QueryParam("session-id") String session_id)
	{
		return remoteControlResource.playpause(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/nextitem")
	public String nextitem(@QueryParam("session-id") String session_id)
	{
		return remoteControlResource.nextitem(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/previtem")
	public String previtem(@QueryParam("session-id") String session_id)
	{
		return remoteControlResource.previtem(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playlist")
	public String playlist(@QueryParam("session-id") String session_id)
	{
		return remoteControlResource.playlist(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/setproperty")
	public String setproperty(@Context UriInfo uriInfo, @QueryParam("session-id") String session_id)
	{
		return remoteControlResource.setproperty(uriInfo, session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/getproperty")
	public String getproperty(@Context UriInfo uriInfo, @QueryParam("session-id") String session_id)
	{
		return remoteControlResource.getproperty(uriInfo, session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playstatusupdate")
	public String playstatusupdate(@QueryParam("session-id") String session_id)
	{
		return remoteControlResource.playstatusupdate(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/cue")
	public String cue(@QueryParam("commmand") String command, @QueryParam("query") String query, @QueryParam("index") String index, @QueryParam("session-id") String session_id)
	{
		return remoteControlResource.cue(command, query, index, session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/getspeakers")
	public String getspeakers(@QueryParam("session-id") String session_id)
	{
		return remoteControlResource.getspeakers(session_id);
	}

	@Override
	@GET
	@Path("ctrl-int/1/setspeakers")
	public String setspeakers(@QueryParam("speaker-id") String speaker_id, @QueryParam("session-id") String session_id)
	{
		return remoteControlResource.setspeakers(speaker_id, session_id);
	}

	@Override
	@GET
	@Path("/ctrl-int/1/playspec")
	public String playspec(@QueryParam("playlist-spec") String playlist_spec, @QueryParam("session-id") String session_id)
	{
		return remoteControlResource.playspec(playlist_spec, session_id);
	}
}
