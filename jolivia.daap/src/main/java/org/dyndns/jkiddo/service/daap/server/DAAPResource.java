package org.dyndns.jkiddo.service.daap.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmap.chunks.audio.AlbumSearchContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.ArtistSearchContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.AudioProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseItems;
import org.dyndns.jkiddo.dmap.chunks.audio.SupportsExtraData;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.MusicSharingVersion;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownMQ;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownSL;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownSR;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownTr;
import org.dyndns.jkiddo.dmp.chunks.VersionChunk;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.chunks.media.DatabaseCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.LoginRequired;
import org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion;
import org.dyndns.jkiddo.dmp.chunks.media.ReturnedCount;
import org.dyndns.jkiddo.dmp.chunks.media.ServerInfoResponse;
import org.dyndns.jkiddo.dmp.chunks.media.SpecifiedTotalCount;
import org.dyndns.jkiddo.dmp.chunks.media.Status;
import org.dyndns.jkiddo.dmp.chunks.media.SupportsAutoLogout;
import org.dyndns.jkiddo.dmp.chunks.media.SupportsBrowse;
import org.dyndns.jkiddo.dmp.chunks.media.SupportsExtensions;
import org.dyndns.jkiddo.dmp.chunks.media.SupportsIndex;
import org.dyndns.jkiddo.dmp.chunks.media.SupportsPersistentIds;
import org.dyndns.jkiddo.dmp.chunks.media.SupportsPlaylistEdit;
import org.dyndns.jkiddo.dmp.chunks.media.SupportsQuery;
import org.dyndns.jkiddo.dmp.chunks.media.SupportsUpdate;
import org.dyndns.jkiddo.dmp.chunks.media.TimeoutInterval;
import org.dyndns.jkiddo.dmp.chunks.media.UpdateType;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion;
import org.dyndns.jkiddo.service.dmap.DMAPResource;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

@Consumes(MediaType.WILDCARD)
// @Produces(MediaType.WILDCARD)
public class DAAPResource extends DMAPResource<IItemManager> implements IMusicLibrary
{
	static final Logger logger = LoggerFactory.getLogger(DAAPResource.class);

	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";
	public static final String DAAP_RESOURCE = "DAAP_IMPLEMENTATION";

	static final String TXT_VERSION = "1";
	static final String TXT_VERSION_KEY = "txtvers";
	static final String DATABASE_ID_KEY = "Database ID";
	static final String MACHINE_ID_KEY = "Machine ID";
	static final String MACHINE_NAME_KEY = "Machine Name";
	static final String MEDIA_KINDS_SHARED_KEY = "Media Kinds Shared";
	static final String ITSH_VERSION_KEY = "iTSh Version";
	static final String VERSION_KEY = "Version";
	static final String PASSWORD_KEY = "Password";

	private static final VersionChunk pictureProtocolVersion = new PictureProtocolVersion(DmapUtil.PPRO_VERSION_201);
	private static final VersionChunk audioProtocolVersion = new AudioProtocolVersion(DmapUtil.APRO_VERSION_3012);
	private static final VersionChunk mediaProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_2010);
	private static final MusicSharingVersion musicSharingVersion = new MusicSharingVersion(DmapUtil.MUSIC_SHARING_VERSION_3011);

	protected final String serviceGuid;

	@Inject
	public DAAPResource(final IZeroconfManager mDNS, @Named(DAAP_PORT_NAME) final Integer port, @Named(Util.APPLICATION_NAME) final String applicationName, @Named(DAAPResource.DAAP_RESOURCE) final IItemManager itemManager) throws IOException
	{
		super(mDNS, port, itemManager);
		this.name = applicationName;
		this.serviceGuid = Util.toServiceGuid(applicationName);
		
		this.register();
	}

	@Override
	protected IZeroconfManager.ServiceInfo getServiceInfoToRegister()
	{
		final String hexedHostname;
		try
		{
			hexedHostname = Util.toHex(hostname.getBytes("UTF-8"));
		}
		catch(final UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
		final HashMap<String, String> records = new HashMap<String, String>();
		records.put(MACHINE_NAME_KEY, name);
		records.put("OSsi", "0x4E8DAC");

		records.put(MEDIA_KINDS_SHARED_KEY, "1");
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(MACHINE_ID_KEY, hexedHostname);
		records.put(VERSION_KEY, audioProtocolVersion.getValue() + "");
		records.put(ITSH_VERSION_KEY, musicSharingVersion.getValue() + "");
		records.put("MID", "0x" + serviceGuid);
		records.put("dmv", mediaProtocolVersion.getValue() + "");
		records.put(DATABASE_ID_KEY, hexedHostname);
		if(PasswordMethod.NO_PASSWORD == itemManager.getAuthenticationMethod())
		{
			records.put(PASSWORD_KEY, "0");
			return new IZeroconfManager.ServiceInfo(DAAP_SERVICE_TYPE, name, port, records);
		}
		records.put(PASSWORD_KEY, "1");
		return new IZeroconfManager.ServiceInfo(DAAP_SERVICE_TYPE, name + "_PW", port, records);
	}

	@Override
	@Path("server-info")
	@GET
	public Response serverInfo(@QueryParam("hsgid") final String hsgid) throws IOException, SQLException
	{
		final ServerInfoResponse serverInfoResponse = new ServerInfoResponse();

		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(mediaProtocolVersion);
		serverInfoResponse.add(new ItemName(name));
		serverInfoResponse.add(audioProtocolVersion);
		serverInfoResponse.add(musicSharingVersion);
		serverInfoResponse.add(new SupportsExtraData(7));
		
		serverInfoResponse.add(pictureProtocolVersion);

//		serverInfoResponse.add(new SupportsGroups(SupportsGroups.SUPPORTS_ARTISTS_AND_ALBUM_GROUPS));
		//serverInfoResponse.add(new SupportsGroups(SupportsGroups.SUPPORTS_ARTISTS_AND_ALBUM_GROUPS));
//		serverInfoResponse.add(new SupportsGroups(SupportsGroups.SUPPORTS_ALBUM_GROUPS));
//		serverInfoResponse.add(new SupportsGroups(SupportsGroups.SUPPORTS_ARTISTS_GROUPS));
		
		
		 serverInfoResponse.add(new UnknownMQ(true));
		 serverInfoResponse.add(new UnknownTr(true));
		 serverInfoResponse.add(new UnknownSL(true));
		 serverInfoResponse.add(new UnknownSR(true));
		 serverInfoResponse.add(new SupportsPlaylistEdit(false));
		
		
		// serverInfoResponse.add(new UnknownSE(0x80000));
		// serverInfoResponse.add(new UnknownFR(0x64));
		// serverInfoResponse.add(new SupportsFairPlay(SupportsFairPlay.UNKNOWN_VALUE));//iTunes 11.0.2.26 says 2. If inserted, DAAP dies
		// serverInfoResponse.add(new UnknownSX(111));
		// Unknownml msml = new Unknownml();
		// msml.add(new UnknownMA(0xBF940AB92600L)); //iTunes 11.0.2.26 - Totally unknown
		// serverInfoResponse.add(msml);
		
		serverInfoResponse.add(new LoginRequired(true));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(true));
		final PasswordMethod authenticationMethod = itemManager.getAuthenticationMethod();
		if(!(authenticationMethod == PasswordMethod.NO_PASSWORD))
		{
			if(authenticationMethod == PasswordMethod.PASSWORD)
			{
				serverInfoResponse.add(new AuthenticationMethod(PasswordMethod.PASSWORD));
			}
			else
			{
				serverInfoResponse.add(new AuthenticationMethod(PasswordMethod.USERNAME_AND_PASSWORD));
			}

//			serverInfoResponse.add(new AuthenticationSchemes(AuthenticationSchemes.BASIC_SCHEME | AuthenticationSchemes.DIGEST_SCHEME));
		}
		else
		{
			serverInfoResponse.add(new AuthenticationMethod(AuthenticationMethod.PasswordMethod.NO_PASSWORD));
		}
		serverInfoResponse.add(new SupportsUpdate(true));
		serverInfoResponse.add(new SupportsPersistentIds(true));
		serverInfoResponse.add(new SupportsExtensions(true));
		serverInfoResponse.add(new SupportsBrowse(true));
		serverInfoResponse.add(new SupportsQuery(true));
		serverInfoResponse.add(new SupportsIndex(true));
//		serverInfoResponse.add(new SupportsResolve(true));
		serverInfoResponse.add(new DatabaseCount(itemManager.getDatabases().size()));
//		serverInfoResponse.add(new UTCTime(Calendar.getInstance().getTimeInMillis() / 1000));
//		serverInfoResponse.add(new UTCTimeOffset(7200));

		return Util.buildResponse(serverInfoResponse, getDMAPKey(), name);
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@PathParam("databaseId") final long databaseId, @PathParam("itemId") final long itemId, @PathParam("format") final String format, @HeaderParam("Range") final String rangeHeader) throws IOException
	{
		final byte[] array = itemManager.getItemAsByteArray(databaseId, itemId);

		final long[] range = getRange(rangeHeader, 0, array.length);
		final int pos = (int) range[0];
		final int end = (int) range[1];

		return Util.buildAudioResponse(Arrays.copyOfRange(array, pos, end), pos, getDMAPKey(), name);

	}

	static private long[] getRange(final String rangeHeader, long position, long end)
	{
		if(!Strings.isNullOrEmpty(rangeHeader))
		{
			final StringTokenizer token = new StringTokenizer(rangeHeader, "=");
			final String key = token.nextToken().trim();

			if(("bytes").equals(key) == false)
			{
				throw new NullPointerException("Format of range header is unknown");
			}
			final StringTokenizer rangesToken = new StringTokenizer(token.nextToken(), "-");
			position = Long.parseLong(rangesToken.nextToken().trim());
			if(rangesToken.hasMoreTokens())
				end = Long.parseLong(rangesToken.nextToken().trim());
		}

		return(new long[] { position, end });
	}

	@Override
	@Path("databases/{databaseId}/items")
	@GET
	public Response items(@PathParam("databaseId") final long databaseId, @QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("type") final String type, @QueryParam("meta") final String meta, @QueryParam("query") final String query, @QueryParam("hsgid") final String hsgid) throws IOException, SQLException
	{
		// dpap: limited by query
		// http://192.168.1.2dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.thumb,dmap.itemid,dpap.filedata&query=('dmap.itemid:2810','dmap.itemid:2811','dmap.itemid:2812','dmap.itemid:2813','dmap.itemid:2814','dmap.itemid:2815','dmap.itemid:2816','dmap.itemid:2817','dmap.itemid:2818','dmap.itemid:2819','dmap.itemid:2820','dmap.itemid:2821','dmap.itemid:2822','dmap.itemid:2823','dmap.itemid:2824','dmap.itemid:2825','dmap.itemid:2826','dmap.itemid:2827','dmap.itemid:2851','dmap.itemid:2852')

		// GET dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.hires,dmap.itemid,dpap.filedata&query=('dmap.itemid:2742') HTTP/1.1

		// .getDatabases(), new Predicate<Database>() {
		// @Override
		// public boolean apply(Database database)
		// {
		// return database.getItemId() == databaseId;
		// }
		// }).getItems();
		final Iterable<String> parameters = DmapUtil.parseMeta(meta);

		final DatabaseItems databaseSongs = new DatabaseItems();
		databaseSongs.add(new Status(200));
		databaseSongs.add(new UpdateType(0));

		final Listing listing = itemManager.getMediaItems(databaseId, parameters);
		databaseSongs.add(new SpecifiedTotalCount(Iterables.size(listing.getListingItems())));
		databaseSongs.add(new ReturnedCount(Iterables.size(listing.getListingItems())));

		/*
		 * for(MediaItem item : items) { ListingItem listingItem = new ListingItem(); if("all".equals(meta)) { listingItem.add(item.getChunk("dmap.itemkind")); for(Chunk chunk : item.getChunks()) { if(chunk.getName().equals("dmap.itemkind")) continue; listingItem.add(chunk); } } else { for(String key : parameters) { Chunk chunk = item.getChunk(key); if(chunk != null) { listingItem.add(chunk); } else { logger.info("Unknown chunk type: " + key); } } } listing.add(listingItem); }
		 */

		databaseSongs.add(listing);

		// if(request.isUpdateType() && deletedSongs != null)
		// {
		// DeletedIdListing deletedListing = new DeletedIdListing();
		//
		// for(Song song : deletedSongs)
		// {
		// deletedListing.add(song.getChunk("dmap.itemid"));
		// }
		//
		// databaseSongs.add(deletedListing);
		// }

		return Util.buildResponse(databaseSongs, getDMAPKey(), name);
	}

	@Override
	// @Path("databases/{databaseId}/groups/{groupdId}/extra_data/artwork")
	@Path("databases/{databaseId}/items/{groupdId}/extra_data/artwork")
	@GET
	public Response artwork(@PathParam("databaseId") final long databaseId, @PathParam("groupId") final long groupId, @QueryParam("session-id") final long sessionId, @QueryParam("mw") final String mw, @QueryParam("mh") final String mh, @QueryParam("group-type") final String group_type, @QueryParam("daapSecInfo") final String daapSecInfo) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") final long databaseId, @QueryParam("meta") final String meta, @QueryParam("type") final String type, @QueryParam("group-type") final String groupType, @QueryParam("sort") final String sort, @QueryParam("include-sort-headers") final long includeSortHeaders, @QueryParam("query") final String query, @QueryParam("session-id") final long sessionId, @QueryParam("hsgid") final String hsgid) throws IOException, SQLException
	{
		final Iterable<String> parameters = DmapUtil.parseMeta(meta);
		if("artists".equalsIgnoreCase(groupType))
		{
			final ArtistSearchContainer response = new ArtistSearchContainer();
			response.add(new Status(200));
			response.add(new UpdateType(0));
			//response.add(new SpecifiedTotalCount(0));//
			//response.add(new ReturnedCount(0));//

			//final Listing listing = new Listing();
			final Listing listing = itemManager.getMediaItems(databaseId, parameters);
			response.add(new SpecifiedTotalCount(Iterables.size(listing.getListingItems())));
			response.add(new ReturnedCount(Iterables.size(listing.getListingItems())));
//			listing.add(new SortingHeaderListing());//
			response.add(listing);

			return Util.buildResponse(response, getDMAPKey(), name);
		}
		else if("albums".equalsIgnoreCase(groupType))
		{
			final AlbumSearchContainer response = new AlbumSearchContainer();
			response.add(new Status(200));
			response.add(new UpdateType(0));
			
			final Listing listing = itemManager.getMediaItems(databaseId, parameters);
		 	response.add(new SpecifiedTotalCount(Iterables.size(listing.getListingItems())));
			response.add(new ReturnedCount(Iterables.size(listing.getListingItems())));

			//final Listing listing = new Listing();
			response.add(listing);

			return Util.buildResponse(response, getDMAPKey(), name);
		}
		else
			throw new NotImplementedException();
	}

	@Override
	public String getDMAPKey()
	{
		return "DAAP-Server";
	}
}
