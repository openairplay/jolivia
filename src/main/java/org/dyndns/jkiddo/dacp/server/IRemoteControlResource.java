package org.dyndns.jkiddo.dacp.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface IRemoteControlResource
{

	@GET
	@Path("login")
	Response login(@QueryParam("pairing-guid") String guid);

	@GET
	@Path("logout")
	String logout(@QueryParam("session-id") String session_id);

//	@GET
//	@Path("databases/{databaseid}/containers")
//	String databases(@PathParam("databaseid") String databaseid, @QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/pause")
	String pause(@QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/stop")
	String stop(@QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/playpause")
	String playpause(@QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/nextitem")
	String nextitem(@QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/previtem")
	String previtem(@QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/playlist")
	String playlist(@QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/setproperty")
	String setproperty(@Context UriInfo uriInfo, @QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/getproperty")
	String getproperty(@Context UriInfo uriInfo, @QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/playstatusupdate")
	String playstatusupdate(@QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/cue")
	String cue(@QueryParam("commmand") String command, @QueryParam("query") String query, @QueryParam("index") String index, @QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/getspeakers")
	String getspeakers(@QueryParam("session-id") String session_id);

	@GET
	@Path("ctrl-int/1/setspeakers")
	String setspeakers(@QueryParam("speaker-id") String speaker_id, @QueryParam("session-id") String session_id);

	@GET
	@Path("/ctrl-int/1/playspec")
	String playspec(@QueryParam("playlist-spec") String playlist_spec, @QueryParam("session-id") String session_id);

}