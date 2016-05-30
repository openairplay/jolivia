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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownFR;
import org.dyndns.jkiddo.dmcp.chunks.media.MediaControlProtocolVersion;
import org.dyndns.jkiddo.dmcp.chunks.media.PlayingStatus;
import org.dyndns.jkiddo.dmcp.chunks.media.PropertyResponse;
import org.dyndns.jkiddo.dmcp.chunks.media.RelativeVolume;
import org.dyndns.jkiddo.dmcp.chunks.media.StatusRevision;
import org.dyndns.jkiddo.dmcp.chunks.media.UnknownIK;
import org.dyndns.jkiddo.dmcp.chunks.media.UnknownRL;
import org.dyndns.jkiddo.dmcp.chunks.media.UnknownSP;
import org.dyndns.jkiddo.dmcp.chunks.media.UnknownSV;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.AudioControlProtocolVersion;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.AvailableRepeatStates;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.AvailableShuffleStates;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.DataControlInt;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.FullScreenEnabled;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.FullscreenStatus;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.PlayStatus;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.RepeatStatus;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.ShuffleStatus;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.SpeakerActive;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.SpeakerList;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownOV;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSS;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSU;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownVD;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.VisualizerEnabled;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.VisualizerStatus;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.VolumeControllable;
import org.dyndns.jkiddo.dmcp.chunks.media.extension.SavedGenius;
import org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownCESX;
import org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQU;
import org.dyndns.jkiddo.dmp.chunks.media.Dictionary;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.MachineAddress;
import org.dyndns.jkiddo.dmp.chunks.media.ReturnedCount;
import org.dyndns.jkiddo.dmp.chunks.media.SpecifiedTotalCount;
import org.dyndns.jkiddo.dmp.chunks.media.Status;
import org.dyndns.jkiddo.dmp.chunks.media.UpdateType;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.service.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;

@Singleton
public class TouchAbleServerResource extends MDNSResource implements ITouchAbleServerResource
{
	public static final String DACP_SERVER_PORT_NAME = "DACP_SERVER_PORT_NAME";
	private final String name;
	private final String serviceGuid;

	@Inject
	public TouchAbleServerResource(final IZeroconfManager mDNS, @Named(DACP_SERVER_PORT_NAME) final Integer port, @Named(Util.APPLICATION_NAME) final String applicationName, final IPairingDatabase pairingDatabase ) throws IOException
	{
		super(mDNS, port);
		this.name = applicationName;
		this.serviceGuid = Util.toServiceGuid(applicationName);
		this.register();
	}

	@Override
	@GET
	@Path("logout")
	public Response logout(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/pause")
	public Response pause(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/stop")
	public Response stop(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/playpause")
	public Response playpause(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/nextitem")
	public Response nextitem(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/previtem")
	public Response previtem(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/playlist")
	public Response playlist(@QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/setproperty")
	public Response setproperty(@Context final UriInfo uriInfo, @QueryParam("dmcp.volume") final String dmcpVolume, @QueryParam("dacp.playingtime") final String dacpPlayingtime, @QueryParam("dacp.shufflestate") final String dacpShufflestate, @QueryParam("dacp.repeatstate") final String dacpRepeatstate, @QueryParam("session-id") final long session_id)
	{
		final MultivaluedMap<String, String> map = uriInfo.getQueryParameters();
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

	@Override
	@GET
	@Path("ctrl-int/1/getproperty")
	public Response getproperty(@Context final UriInfo uriInfo, @QueryParam("properties") final String properties, @QueryParam("session-id") final long session_id, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		final MultivaluedMap<String, String> map = uriInfo.getQueryParameters();
		map.get("properties");

		final PropertyResponse response = new PropertyResponse();
		response.add(new Status(200));
		response.add(new RelativeVolume(100));//
		return Util.buildResponse(response, "DAAP-Server", name);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playstatusupdate")
	public Response playstatusupdate(@QueryParam("revision-number") final long revisionNumber, @QueryParam("session-id") final long session_id, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		if(revisionNumber == 0x24)
		{
			try
			{
				Thread.sleep(10000000);
			}
			catch(final InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final PlayingStatus response = new PlayingStatus();
		response.add(new Status(200));
		response.add(new StatusRevision(0x24));//
		response.add(new PlayStatus(2));//
		response.add(new ShuffleStatus(0));
		response.add(new RepeatStatus(0));
		response.add(new FullscreenStatus(0));
		response.add(new VisualizerStatus(0));
		response.add(new VolumeControllable(0));
		response.add(new AvailableShuffleStates(2));//
		response.add(new AvailableRepeatStates(6));//
		response.add(new FullScreenEnabled(0));
		response.add(new VisualizerEnabled(0));
		response.add(new UnknownSU(false));
		response.add(new UnknownQU(0));
		return Util.buildResponse(response, "DAAP-Server", name);
	}

	@Override
	@GET
	@Path("ctrl-int/1/cue")
	public Response cue(@QueryParam("commmand") final String command, @QueryParam("query") final String query, @QueryParam("index") final String index, @QueryParam("sort") final String sort, @QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int/1/getspeakers")
	public Response getspeakers(@QueryParam("session-id") final long session_id, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		final SpeakerList response = new SpeakerList();
		response.add(new Status(200));
		final Dictionary dictionary = new Dictionary();
		dictionary.add(new ItemName("My Compounter"));
		dictionary.add(new RelativeVolume(100));
		dictionary.add(new UnknownVD(true));
		dictionary.add(new MachineAddress(new byte[]{0}));
		dictionary.add(new SpeakerActive(true));

		response.add(dictionary);
		return Util.buildResponse(response, "DAAP-Server", name);
	}

	@Override
	@GET
	@Path("ctrl-int/1/setspeakers")
	public Response setspeakers(@QueryParam("speaker-id") final String speaker_id, @QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("/ctrl-int/1/playspec")
	public Response playspec(@QueryParam("container-item-spec") final String container_item_spec, @QueryParam("item-spec") final String item_spec, @QueryParam("container-spec") final String container_spec, @QueryParam("dacp.shufflestate") final String dacp_shufflestate, @QueryParam("database-spec") final String database_spec, @QueryParam("playlist-spec") final String playlist_spec, @QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("/ctrl-int/1/nowplayingartwork")
	public Response nowplayingartwork(@QueryParam("mw") final String mw, @QueryParam("mh") final String mh, @QueryParam("session-id") final long session_id, @QueryParam("hsgid") final String hsgid)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("/ctrl-int/1/set-genius-seed")
	public Response editGenius(@QueryParam("database-spec") final String database_spec, @QueryParam("item-spec") final String item_spec, @QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/edit")
	@GET
	public Response editPlaylist(@PathParam("databaseId") final long databaseId, @QueryParam("session-id") final long sessionId, @QueryParam("action") final String action, @QueryParam("edit-params") final String edit_params) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	protected IZeroconfManager.ServiceInfo getServiceInfoToRegister()
	{
		try
		{
			Util.toHex(hostname.getBytes("UTF-8"));
		}
		catch(final UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final HashMap<String, String> records = new HashMap<>();
		records.put("Ver", DmapUtil.CONTROL_VERSION_205 +"");
		records.put("CtlN", name);
		records.put("OSsi", "0x1F5");
		records.put("iV", "196620");
//		records.put("DbId", hexedHostname);
		records.put("DbId", DB_ID);
		records.put("DvTy", "iTunes");
		records.put("txtvers", "1");
		records.put("iCSV", "65540");
		records.put("DvSv", "3120");

//		return new IZeroconfManager.ServiceInfo(TOUCH_ABLE_SERVER, serviceGuid, port, records);
		return new IZeroconfManager.ServiceInfo(TOUCH_ABLE_SERVER, MID, port, records);
	}

	@Override
	@GET
	@Path("ctrl-int/1/playqueue-contents")
	public Response playQueueContents(@QueryParam("span") final int span, @QueryParam("session-id") final long session_id)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("fp-setup")
	public Response fpSetup(@QueryParam("session-id") final long session_id, @QueryParam("hsgid") final String hsgid, final InputStream is)
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("login")
	public Response login(@QueryParam("pairing-guid") final String guid, @QueryParam("hasFP") final int value, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("update")
	@GET
	public Response update(@QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("daap-no-disconnect") final int daapNoDisconnect, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	@GET
	@Path("ctrl-int")
	public Response ctrlInt(@Context final HttpServletRequest httpServletRequest, @Context final HttpServletResponse httpServletResponse, @QueryParam("hsgid") final String hsgid) throws IOException
	{

		final DataControlInt caci = new DataControlInt();
		caci.add(new Status(200));
		caci.add(new UpdateType(0));
		caci.add(new SpecifiedTotalCount(4));//
		caci.add(new ReturnedCount(1));//
		final Listing listing = new Listing();
		final ListingItem item = new ListingItem();
		item.add(new ItemId(1));//
		item.add(new UnknownIK(true));
		item.add(new MediaControlProtocolVersion(0x20001));//
		item.add(new AudioControlProtocolVersion(0x20003));//
		item.add(new UnknownSP(true));
		item.add(new UnknownFR(100));
		item.add(new UnknownSV(true));
		item.add(new UnknownSS(true));
		item.add(new UnknownOV(true));
		item.add(new UnknownSU(true));
		item.add(new SavedGenius(true));
		item.add(new UnknownRL(true));
		item.add(new UnknownCESX(1));
		listing.add(item);
		caci.add(listing);
		return Util.buildResponse(caci, "DAAP-Server", name);
	}

	// /ctrl-int/1/playqueue-edit?command=add&query='dmap.itemid:1024'&queuefilter=playlist:1&sort=name&mode=1&session-id=42
	@Override
	@GET
	@Path("/ctrl-int/1/playqueue-edit")
	public Response playQueueEdit(@Context final HttpServletRequest httpServletRequest, @Context final HttpServletResponse httpServletResponse, @QueryParam("commmand") final String command, @QueryParam("query") final String query, @QueryParam("queuefilter") final String index, @QueryParam("sort") final String sort, @QueryParam("session-id") final long session_id) throws Exception
	{
		return Util.buildEmptyResponse("DAAP-Server", name);
	}

	@Override
	@GET
	@Path("server-info")
	public Response serverInfo(@QueryParam("hsgid") final String hsgid) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@GET
	@Path("home-share-verify")
	public Response homeShareVerify(@QueryParam("session-id") final long session_id, @QueryParam("hsgid") final String hsgid, @QueryParam("hspid") final String hspid)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@PathParam("containerId") final long containerId, @PathParam("databaseId") final long databaseId, @QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("meta") final String meta, @QueryParam("type") final String type, @QueryParam("group-type") final String group_type, @QueryParam("sort") final String sort, @QueryParam("include-sort-headers") final String include_sort_headers, @QueryParam("query") final String query, @QueryParam("index") final String index, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("databases")
	@GET
	public Response databases(@QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("databases/{databaseId}/containers")
	@GET
	public Response containers(@PathParam("databaseId") final long databaseId, @QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("meta") final String meta, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Path("databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") final long databaseId, @QueryParam("meta") final String meta, @QueryParam("type") final String type, @QueryParam("group-type") final String groupType, @QueryParam("sort") final String sort, @QueryParam("include-sort-headers") final long includeSortHeaders, @QueryParam("query") final String query, @QueryParam("session-id") final long sessionId, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response artwork(final long databaseId, final String mw, final String mh,
			final long sessionId, final String groupType, final String hsgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response artists(final long databaseId, final long sessionId,
			final long includeSortHeaders, final String filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
