package org.dyndns.jkiddo.service.daap.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.MediaItem;
import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.audio.AlbumSearchContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.ArtistSearchContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseItems;
import org.dyndns.jkiddo.dmap.chunks.audio.SupportsExtraData;
import org.dyndns.jkiddo.dmap.chunks.audio.SupportsGroups;
import org.dyndns.jkiddo.dmap.chunks.media.AuthenticationMethod;
import org.dyndns.jkiddo.dmap.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmap.chunks.media.AuthenticationSchemes;
import org.dyndns.jkiddo.dmap.chunks.media.DatabaseCount;
import org.dyndns.jkiddo.dmap.chunks.media.ItemName;
import org.dyndns.jkiddo.dmap.chunks.media.Listing;
import org.dyndns.jkiddo.dmap.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmap.chunks.media.LoginRequired;
import org.dyndns.jkiddo.dmap.chunks.media.ReturnedCount;
import org.dyndns.jkiddo.dmap.chunks.media.ServerInfoResponse;
import org.dyndns.jkiddo.dmap.chunks.media.SpecifiedTotalCount;
import org.dyndns.jkiddo.dmap.chunks.media.Status;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsAutoLogout;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsBrowse;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsExtensions;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsIndex;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsPersistentIds;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsQuery;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsResolve;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsUpdate;
import org.dyndns.jkiddo.dmap.chunks.media.TimeoutInterval;
import org.dyndns.jkiddo.dmap.chunks.media.UTCTime;
import org.dyndns.jkiddo.dmap.chunks.media.UTCTimeOffset;
import org.dyndns.jkiddo.dmap.chunks.media.UnknownHL;
import org.dyndns.jkiddo.dmap.chunks.media.UpdateType;
import org.dyndns.jkiddo.dmap.chunks.unknown.Voting;
import org.dyndns.jkiddo.dmcp.chunks.media.extension.WelcomeMessage;
import org.dyndns.jkiddo.service.dmap.DMAPResource;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

@Consumes(MediaType.WILDCARD)
// @Produces(MediaType.WILDCARD)
public class DAAPResource extends DMAPResource<IItemManager> implements IMusicLibrary
{
	static final Logger logger = LoggerFactory.getLogger(DAAPResource.class);

	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";
	public static final String DAAP_RESOURCE = "DAAP_IMPLEMENTATION";

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DATABASE_ID_KEY = "Database ID";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String MACHINE_NAME_KEY = "Machine Name";
	private static final String ITSH_VERSION_KEY = "iTSh Version";
	private static final String DAAP_VERSION_KEY = "Version";
	private static final String PASSWORD_KEY = "Password";
	private final String serviceGuid;

	@Inject
	public DAAPResource(JmmDNS mDNS, @Named(DAAP_PORT_NAME) Integer port, @Named(Util.APPLICATION_NAME) String applicationName, @Named(DAAPResource.DAAP_RESOURCE) MusicItemManager itemManager) throws IOException
	{
		super(mDNS, port, itemManager);
		this.name = applicationName;
		this.serviceGuid = Util.toServiceGuid(applicationName);
		this.signUp();
	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		final String hexedHostname;
		try
		{
			hexedHostname = Util.toHex(hostname.getBytes("UTF-8"));
		}
		catch(UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(MACHINE_NAME_KEY, name);
		records.put("OSsi", "0x4E8DAC");
		records.put(PASSWORD_KEY, "0");
		records.put("Media Kinds Shared", "0");
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(MACHINE_ID_KEY, hexedHostname);
		records.put(DAAP_VERSION_KEY, DmapUtil.APRO_VERSION_3011 + "");
		records.put(ITSH_VERSION_KEY, DmapUtil.MUSIC_SHARING_VERSION_309 + "");
		records.put("MID", "0x" + serviceGuid);
		records.put("dmc", "131081");
		records.put(DATABASE_ID_KEY, hexedHostname);

		return ServiceInfo.create(DAAP_SERVICE_TYPE, name, port, 0, 0, records);
	}

	@Override
	@Path("server-info")
	@GET
	public Response serverInfo() throws IOException
	{
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();

		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(itemManager.getMediaProtocolVersion());
		serverInfoResponse.add(new ItemName(name));
		serverInfoResponse.add(itemManager.getAudioProtocolVersion());
		// serverInfoResponse.add(itemManager.getMusicSharingVersion()); If inserted, DAAP dies

		serverInfoResponse.add(new SupportsExtraData(3));
		serverInfoResponse.add(new WelcomeMessage("jgjgjhgjgjhgjgyutrutuolmæ"));
		serverInfoResponse.add(new Voting(true));

		serverInfoResponse.add(new SupportsExtensions(true));
		serverInfoResponse.add(new SupportsGroups(3));
		// serverInfoResponse.add(new UnknownSE(0x80000));
		// serverInfoResponse.add(new UnknownMQ(true));
		// serverInfoResponse.add(new UnknownFR(0x64));
		// serverInfoResponse.add(new UnknownTr(true));
		// serverInfoResponse.add(new UnknownSL(true));
		// serverInfoResponse.add(new UnknownSR(true));
		// serverInfoResponse.add(new SupportsFairPlay(SupportsFairPlay.UNKNOWN_VALUE));//iTunes 11.0.2.26 says 2. If inserted, DAAP dies
		// serverInfoResponse.add(new UnknownSX(111));
		serverInfoResponse.add(itemManager.getPictureProtocolVersion());
		// serverInfoResponse.add(new Unknowned(true));
		// Unknownml msml = new Unknownml();
		// msml.add(new UnknownMA(0xBF940AB92600L)); //iTunes 11.0.2.26 - Totally unknown
		// serverInfoResponse.add(msml);
		serverInfoResponse.add(new LoginRequired(true));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(true));
		PasswordMethod authenticationMethod = itemManager.getAuthenticationMethod();
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

			serverInfoResponse.add(new AuthenticationSchemes(AuthenticationSchemes.BASIC_SCHEME | AuthenticationSchemes.DIGEST_SCHEME));
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
		serverInfoResponse.add(new SupportsResolve(true));
		serverInfoResponse.add(new DatabaseCount(itemManager.getDatabases().size()));
		serverInfoResponse.add(new UTCTime(Calendar.getInstance().getTimeInMillis()));
		serverInfoResponse.add(new UTCTimeOffset(7200));

		return Util.buildResponse(serverInfoResponse, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws IOException
	{
		byte[] array = itemManager.getItemAsByteArray(databaseId, itemId);

		long[] range = getRange(rangeHeader, 0, array.length);
		int pos = (int) range[0];
		int end = (int) range[1];
		byte[] buffer = new byte[end - pos];
		System.arraycopy(array, pos, buffer, 0, buffer.length);
		return Util.buildAudioResponse(buffer, pos, buffer.length, itemManager.getDMAPKey(), name);

	}

	static private long[] getRange(String rangeHeader, long position, long end)
	{
		if(!Strings.isNullOrEmpty(rangeHeader))
		{
			StringTokenizer token = new StringTokenizer(rangeHeader, "=");
			String key = token.nextToken().trim();

			if(("bytes").equals(key) == false)
			{
				throw new NullPointerException("Format of range header is unknown");
			}
			StringTokenizer rangesToken = new StringTokenizer(token.nextToken(), "-");
			position = Long.parseLong(rangesToken.nextToken().trim());
			if(rangesToken.hasMoreTokens())
				end = Long.parseLong(rangesToken.nextToken().trim());
		}

		return(new long[] { position, end });
	}

	@Override
	@Path("databases/{databaseId}/items")
	@GET
	public Response items(@PathParam("databaseId") final long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta, @QueryParam("query") String query) throws IOException
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
		Collection<MediaItem> items = itemManager.getDatabase(databaseId).getItems();

		Iterable<String> parameters = DmapUtil.parseMeta(meta);

		DatabaseItems databaseSongs = new DatabaseItems();

		databaseSongs.add(new Status(200));
		databaseSongs.add(new UpdateType(0));
		databaseSongs.add(new SpecifiedTotalCount(items.size()));

		databaseSongs.add(new ReturnedCount(items.size()));

		Listing listing = new Listing();
		for(MediaItem item : items)
		{
			ListingItem listingItem = new ListingItem();

			if("all".equals(meta))
			{
				listingItem.add(item.getChunk("dmap.itemkind"));
				for(Chunk chunk : item.getChunks())
				{
					if(chunk.getName().equals("dmap.itemkind"))
						continue;
					listingItem.add(chunk);
				}
			}
			else
			{
				for(String key : parameters)
				{
					Chunk chunk = item.getChunk(key);

					if(chunk != null)
					{
						listingItem.add(chunk);
					}
					else
					{
						logger.info("Unknown chunk type: " + key);
					}
				}
			}

			listing.add(listingItem);
		}

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

		return Util.buildResponse(databaseSongs, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("databases/{databaseId}/groups/{groupdId}/extra_data/artwork")
	@GET
	public Response artwork(@PathParam("databaseId") long databaseId, @PathParam("groupId") long groupId, @QueryParam("session-id") long sessionId, @QueryParam("mw") String mw, @QueryParam("mh") String mh, @QueryParam("group-type") String group_type) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws IOException
	{

		if("artists".equalsIgnoreCase(group_type))
		{
			ArtistSearchContainer response = new ArtistSearchContainer();
			response.add(new Status(200));
			response.add(new UpdateType(0));
			response.add(new SpecifiedTotalCount(0));//
			response.add(new ReturnedCount(0));//

			Listing listing = new Listing();
			listing.add(new UnknownHL());//
			response.add(listing);

			return Util.buildResponse(response, itemManager.getDMAPKey(), name);
		}
		else if("albums".equalsIgnoreCase(group_type))
		{
			AlbumSearchContainer response = new AlbumSearchContainer();
			response.add(new Status(200));
			response.add(new UpdateType(0));
			response.add(new SpecifiedTotalCount(0));//
			response.add(new ReturnedCount(0));//

			Listing listing = new Listing();
			response.add(listing);

			return Util.buildResponse(response, itemManager.getDMAPKey(), name);
		}
		else
			throw new NotImplementedException();
	}
}
