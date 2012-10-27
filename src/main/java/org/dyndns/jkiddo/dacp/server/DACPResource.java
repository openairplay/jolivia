package org.dyndns.jkiddo.dacp.server;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmap.DMAPResource;

public class DACPResource extends DMAPResource
{
	@Inject
	public DACPResource(JmDNS mDNS, Integer port) throws IOException
	{
		super(mDNS, port);
	}

	@GET
	@Path("login")
	public String login(@QueryParam("pairing-guid") final String guid)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("logout")
	public String logout(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("databases/{databaseid}/containers")
	public String databases(@PathParam("databaseid") final String databaseid, @QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/pause")
	public String pause(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/stop")
	public String stop(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/playpause")
	public String playpause(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/nextitem")
	public String nextitem(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/previtem")
	public String previtem(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/playlist")
	public String playlist(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/setproperty")
	public String setproperty(@Context final UriInfo uriInfo, @QueryParam("session-id") final String session_id)
	{
		MultivaluedMap<String, String> map = uriInfo.getQueryParameters();
		map.get("dmcp.volume");
		map.get("dacp.playingtime");
		map.get("dacp.shufflestate");
		map.get("dacp.repeatstate");
		map.get("dacp.userrating");
		map.get("song-spec");
		map.get("speaker-id");
		map.get("include-speaker-id");
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/getproperty")
	public String getproperty(@Context final UriInfo uriInfo, @QueryParam("session-id") final String session_id)
	{
		MultivaluedMap<String, String> map = uriInfo.getQueryParameters();
		map.get("properties");
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/playstatusupdate")
	public String playstatusupdate(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/cue")
	public String cue(@QueryParam("commmand") final String command, @QueryParam("query") final String query, @QueryParam("index") final String index, @QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/getspeakers")
	public String getspeakers(@QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("ctrl-int/1/setspeakers")
	public String setspeakers(@QueryParam("speaker-id") final String speaker_id, @QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@GET
	@Path("/ctrl-int/1/playspec")
	public String playspec(@QueryParam("playlist-spec") final String playlist_spec, @QueryParam("session-id") final String session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	protected ServiceInfo registerServerRemote()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);

		final HashMap<String, String> records = new HashMap<String, String>();
		records.put("CtlN", "BrickTunes on " + hostname);
		records.put("OSsi", "0x1F6");
		records.put("Ver", "131073");
		records.put("txtvers", "1");
		records.put("DvTy", "iTunes(JKiddo Inc)");
		records.put("DvSv", "2049");
		records.put("DbId", hash);

		return ServiceInfo.create("_touch-able._tcp.local.", hash, port, 0, 0, records);
	}
}
