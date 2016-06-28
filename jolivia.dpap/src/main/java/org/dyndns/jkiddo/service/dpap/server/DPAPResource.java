package org.dyndns.jkiddo.service.dpap.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseItems;
import org.dyndns.jkiddo.dmp.chunks.VersionChunk;
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
import org.dyndns.jkiddo.dmp.chunks.media.SupportsIndex;
import org.dyndns.jkiddo.dmp.chunks.media.TimeoutInterval;
import org.dyndns.jkiddo.dmp.chunks.media.UpdateType;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion;
import org.dyndns.jkiddo.service.dmap.DMAPResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Consumes(MediaType.WILDCARD)
// @Produces(MediaType.WILDCARD)
public class DPAPResource extends DMAPResource<ImageItemManager> implements IImageLibrary
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DPAPResource.class);

	public static final String DPAP_SERVER_PORT_NAME = "DPAP_SERVER_PORT_NAME";
	public static final String DPAP_RESOURCE = "DPAP_IMPLEMENTATION";
	
	private static final VersionChunk dpapProtocolVersion = new PictureProtocolVersion(DmapUtil.PPRO_VERSION_101);
	private static final VersionChunk dmapProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_200);

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DPAP_VERSION_KEY = "Version";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String IPSH_VERSION_KEY = "iPSh Version";
	private static final String PASSWORD_KEY = "Password";

	@Inject
	public DPAPResource(final IZeroconfManager mDNS, @Named(DPAP_SERVER_PORT_NAME) final Integer port, @Named(Util.APPLICATION_NAME) final String applicationName, @Named(DPAP_RESOURCE) final ImageItemManager itemManager) throws IOException
	{
		super(mDNS, port, itemManager);
		this.name = applicationName;
		this.register();
	}

	@Override
	@Path("server-info")
	@GET
	public Response serverInfo(@QueryParam("hsgid") final String hsgid) throws IOException, SQLException
	{
		final ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(dmapProtocolVersion);
		serverInfoResponse.add(dpapProtocolVersion);
		serverInfoResponse.add(new ItemName(name));
		serverInfoResponse.add(new LoginRequired(false));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(false));
		serverInfoResponse.add(new SupportsIndex(false));
		serverInfoResponse.add(new DatabaseCount(itemManager.getDatabases().size()));

		return Util.buildResponse(serverInfoResponse, getDMAPKey(), name);
	}

	@Override
	protected IZeroconfManager.ServiceInfo getServiceInfoToRegister()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);
		final HashMap<String, String> records = new HashMap<>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DPAP_VERSION_KEY, DmapUtil.PPRO_VERSION_201 + "");
		records.put(IPSH_VERSION_KEY, DmapUtil.PPRO_VERSION_201 + "");
		records.put(MACHINE_ID_KEY, hash);
		records.put(PASSWORD_KEY, "0");

		return new IZeroconfManager.ServiceInfo(DPAP_SERVICE_TYPE, name, port, records);
	}

	@Override
	@Path("databases/{databaseId}/items")
	@GET
	public Response items(@PathParam("databaseId") final long databaseId, @QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("type") final String type, @QueryParam("meta") final String meta, @QueryParam("query") final String query, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		// dpap: limited by query
		// GET dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.thumb,dmap.itemid,dpap.filedata&query=('dmap.itemid:2770','dmap.itemid:2771','dmap.itemid:2772','dmap.itemid:2773','dmap.itemid:2774','dmap.itemid:2775','dmap.itemid:2776','dmap.itemid:2777','dmap.itemid:2778','dmap.itemid:2779','dmap.itemid:2780','dmap.itemid:2781','dmap.itemid:2782','dmap.itemid:2783','dmap.itemid:2784','dmap.itemid:2785','dmap.itemid:2786','dmap.itemid:2787','dmap.itemid:2788','dmap.itemid:2789') HTTP/1.1

		// GET dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.hires,dmap.itemid,dpap.filedata&query=('dmap.itemid:2742') HTTP/1.1

		// look at meta to determine if it should be full size or not

		final Collection<MediaItem> items = getMediaItems(databaseId, query);
		DmapUtil.parseMeta(meta);

		final DatabaseItems databaseItems = new DatabaseItems();

		databaseItems.add(new Status(200));
		databaseItems.add(new UpdateType(0));
		databaseItems.add(new SpecifiedTotalCount(items.size()));
		databaseItems.add(new ReturnedCount(items.size()));

		final Listing listing = new Listing();
		/*for(MediaItem item : items)
		{
			ListingItem listingItem = new ListingItem();

			listingItem.add(item.getChunk("dmap.itemkind"));
			
			for(String key : metaParameters)
			{
				Chunk chunk = item.getChunk(key);

				if(chunk != null)
				{
					listingItem.add(chunk);
				}
				else if("dpap.thumb".equals(key))
				{
					listingItem.add(new FileData(itemManager.getThumb(databaseId, ((ItemId) item.getChunk("dmap.itemid")).getUnsignedValue())));
				}
				else if("dpap.hires".equals(key))
				{
					listingItem.add(new FileData(itemManager.getItemAsByteArray(databaseId, ((ItemId) item.getChunk("dmap.itemid")).getUnsignedValue())));
				}
				else if("dpap.filedata".equals(key))
				{
					continue;
				}
				else
					logger.info("Unknown chunk type: " + key);
				// if("dpap.filedata".equals(chunk.getName()) && !isThumbRequest)
				// {
				// listingItem.add(new FileData(itemManager.getItemAsByteArray(databaseId, ((ItemId) item.getChunk("dmap.itemid")).getUnsignedValue())));
				// }
				// else if("dpap.filedata".equals(chunk.getName()) && isThumbRequest)
				// {
				// listingItem.add(new FileData(itemManager.getThumb(databaseId, ((ItemId) item.getChunk("dmap.itemid")).getUnsignedValue())));
				// }
				// else
				// }
				// else
				// {
				// if(isThumbRequest && "dpap.thumb".equals(key))
				// continue;
				// if(!isThumbRequest && "dpap.hires".equals(key))
				// continue;
				// logger.info("Unknown chunk type: " + key);
				// }
			}

			listing.add(listingItem);
		}*/

		databaseItems.add(listing);

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

		return Util.buildResponse(databaseItems, getDMAPKey(), name);
	}
	private Collection<MediaItem> getMediaItems(final long databaseId, final String query)
	{
		/*final Collection<Integer> itemIds = Collections2.transform(Sets.newHashSet(Splitter.on(",").split(query.replace("(", "").replace(")", ""))), new Function<String, Integer>() {
			public Integer apply(String s)
			{
				if(s.startsWith("'dmap.itemid:"))
					return Integer.parseInt(s.replace("'", "").split(":")[1]);
				return null;
			}
		});

		Collection<MediaItem> items = Collections2.filter(itemManager.getDatabase(databaseId).getItems(), new Predicate<MediaItem>() {

			@Override
			public boolean apply(MediaItem obj)
			{
				ItemId chunk = (ItemId) obj.getChunk("dmap.itemid");
				return itemIds.contains(new Integer(chunk.getValue()));
			}
		});
		return items;*/
		return null;
	}

	@Override
	@Path("this_request_is_simply_to_send_a_close_connection_header")
	@GET
	public Response closeConnection() throws IOException
	{
		return Util.buildEmptyResponse(getDMAPKey(), name);
	}

	@Override
	public String getDMAPKey()
	{
		return "DPAP-Server";
	}
}
