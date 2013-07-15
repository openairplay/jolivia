package org.dyndns.jkiddo.service.dpap.server;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.MediaItem;
import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseItems;
import org.dyndns.jkiddo.dmap.chunks.media.DatabaseCount;
import org.dyndns.jkiddo.dmap.chunks.media.ItemId;
import org.dyndns.jkiddo.dmap.chunks.media.ItemName;
import org.dyndns.jkiddo.dmap.chunks.media.Listing;
import org.dyndns.jkiddo.dmap.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmap.chunks.media.LoginRequired;
import org.dyndns.jkiddo.dmap.chunks.media.ReturnedCount;
import org.dyndns.jkiddo.dmap.chunks.media.ServerInfoResponse;
import org.dyndns.jkiddo.dmap.chunks.media.SpecifiedTotalCount;
import org.dyndns.jkiddo.dmap.chunks.media.Status;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsAutoLogout;
import org.dyndns.jkiddo.dmap.chunks.media.SupportsIndex;
import org.dyndns.jkiddo.dmap.chunks.media.TimeoutInterval;
import org.dyndns.jkiddo.dmap.chunks.media.UpdateType;
import org.dyndns.jkiddo.dmap.chunks.picture.FileData;
import org.dyndns.jkiddo.service.dmap.DMAPResource;
import org.dyndns.jkiddo.service.dmap.Util;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

@Consumes(MediaType.WILDCARD)
// @Produces(MediaType.WILDCARD)
public class DPAPResource extends DMAPResource<ImageItemManager> implements IImageLibrary
{
	public static final String DPAP_SERVER_PORT_NAME = "DPAP_SERVER_PORT_NAME";
	public static final String DPAP_RESOURCE = "DPAP_IMPLEMENTATION";

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DPAP_VERSION_KEY = "Version";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String IPSH_VERSION_KEY = "iPSh Version";
	private static final String PASSWORD_KEY = "Password";

	@Inject
	public DPAPResource(JmmDNS mDNS, @Named(DPAP_SERVER_PORT_NAME) Integer port, @Named(Util.APPLICATION_NAME) String applicationName, @Named(DPAP_RESOURCE) ImageItemManager itemManager) throws IOException
	{
		super(mDNS, port, itemManager);
		this.name = applicationName;
		this.signUp();
	}

	@Override
	@Path("server-info")
	@GET
	public Response serverInfo() throws IOException
	{
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(itemManager.getMediaProtocolVersion());
		serverInfoResponse.add(itemManager.getPictureProtocolVersion());
		serverInfoResponse.add(new ItemName(name));
		serverInfoResponse.add(new LoginRequired(false));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(false));
		serverInfoResponse.add(new SupportsIndex(false));
		serverInfoResponse.add(new DatabaseCount(itemManager.getDatabases().size()));

		return Util.buildResponse(serverInfoResponse, itemManager.getDMAPKey(), name);
	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DPAP_VERSION_KEY, DmapUtil.PPRO_VERSION_200 + "");
		records.put(IPSH_VERSION_KEY, DmapUtil.PPRO_VERSION_200 + "");
		records.put(MACHINE_ID_KEY, hash);
		records.put(PASSWORD_KEY, "0");

		return ServiceInfo.create(DPAP_SERVICE_TYPE, name, port, 0, 0, records);
	}

	@Override
	@Path("databases/{databaseId}/items")
	@GET
	public Response items(@PathParam("databaseId") final long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta, @QueryParam("query") String query) throws IOException
	{
		// dpap: limited by query
		// GET dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.thumb,dmap.itemid,dpap.filedata&query=('dmap.itemid:2770','dmap.itemid:2771','dmap.itemid:2772','dmap.itemid:2773','dmap.itemid:2774','dmap.itemid:2775','dmap.itemid:2776','dmap.itemid:2777','dmap.itemid:2778','dmap.itemid:2779','dmap.itemid:2780','dmap.itemid:2781','dmap.itemid:2782','dmap.itemid:2783','dmap.itemid:2784','dmap.itemid:2785','dmap.itemid:2786','dmap.itemid:2787','dmap.itemid:2788','dmap.itemid:2789') HTTP/1.1

		// GET dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.hires,dmap.itemid,dpap.filedata&query=('dmap.itemid:2742') HTTP/1.1

		// look at meta to determine if it should be full size or not

		Collection<MediaItem> items = getMediaItems(databaseId, query);
		Collection<String> metaParameters = DmapUtil.parseMeta(meta);
		boolean isThumbRequest = metaParameters.contains("dpap.thumb");

		DatabaseItems databaseItems = new DatabaseItems();

		databaseItems.add(new Status(200));
		databaseItems.add(new UpdateType(0));
		databaseItems.add(new SpecifiedTotalCount(items.size()));
		databaseItems.add(new ReturnedCount(items.size()));

		Listing listing = new Listing();
		for(MediaItem item : items)
		{
			ListingItem listingItem = new ListingItem();

			listingItem.add(item.getChunk("dmap.itemkind"));

			for(String key : metaParameters)
			{
				Chunk chunk = item.getChunk(key);

				if(chunk != null)
				{
					if("dpap.filedata".equals(chunk.getName()) && !isThumbRequest)
					{
						byte[] rawData = itemManager.getItemAsByteArray(databaseId, ((ItemId) item.getChunk("dmap.itemid")).getUnsignedValue());
						listingItem.add(new FileData(rawData));
					}
					else
						listingItem.add(chunk);
				}
				else
				{
					logger.info("Unknown chunk type: " + key);
				}
			}

			listing.add(listingItem);
		}

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

		return Util.buildResponse(databaseItems, itemManager.getDMAPKey(), name);
	}
	private Collection<MediaItem> getMediaItems(final long databaseId, final String query)
	{
		final Collection<Integer> itemIds = Collections2.transform(Sets.newHashSet(Splitter.on(",").split(query.replace("(", "").replace(")", ""))), new Function<String, Integer>() {
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
		return items;
	}
}
