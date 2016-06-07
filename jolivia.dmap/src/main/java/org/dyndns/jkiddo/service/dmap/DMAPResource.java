package org.dyndns.jkiddo.service.dmap;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseContainerns;
import org.dyndns.jkiddo.dmap.chunks.audio.ItemsContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.ServerDatabases;
import org.dyndns.jkiddo.dmp.chunks.ContentCodesResponseImpl;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.LoginResponse;
import org.dyndns.jkiddo.dmp.chunks.media.ReturnedCount;
import org.dyndns.jkiddo.dmp.chunks.media.ServerRevision;
import org.dyndns.jkiddo.dmp.chunks.media.SessionId;
import org.dyndns.jkiddo.dmp.chunks.media.SpecifiedTotalCount;
import org.dyndns.jkiddo.dmp.chunks.media.Status;
import org.dyndns.jkiddo.dmp.chunks.media.UpdateResponse;
import org.dyndns.jkiddo.dmp.chunks.media.UpdateType;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Consumes(MediaType.WILDCARD)
@Produces(MediaType.WILDCARD)
public abstract class DMAPResource<T extends IItemManager> extends MDNSResource implements ILibraryResource
{
	final protected T itemManager;
	protected String name;

	public DMAPResource(final IZeroconfManager mDNS, final Integer port, final T itemManager) throws IOException
	{
		super(mDNS, port);
		this.itemManager = itemManager;
	}

	@Override
	@Path("login")
	@GET
	public Response login(@QueryParam("pairing-guid") final String guid, @QueryParam("hasFP") final int value, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		final String s = String.valueOf(Thread.currentThread().getId());
		final LoginResponse loginResponse = new LoginResponse();
		loginResponse.add(new Status(200));
		loginResponse.add(new SessionId(itemManager.getSessionId(s)));
		return Util.buildResponse(loginResponse, getDMAPKey(), name);
	}

	@Override
	@Path("update")
	@GET
	public Response update(@QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("daap-no-disconnect") final int daapNoDisconnect, @QueryParam("hsgid") final String hsgid) throws IOException
	{
		final String s = String.valueOf(Thread.currentThread().getId());
		if(revisionNumber == delta || revisionNumber == itemManager.getRevision(s, sessionId))
		{
			itemManager.waitForUpdate();
		}
		final UpdateResponse updateResponse = new UpdateResponse();
		updateResponse.add(new Status(200));
		updateResponse.add(new ServerRevision(itemManager.getRevision(s, sessionId)));
		return Util.buildResponse(updateResponse, getDMAPKey(), name);
	}

	@Override
	@Path("databases")
	@GET
	public Response databases(@QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("hsgid") final String hsgid) throws IOException, SQLException
	{
		final ServerDatabases serverDatabases = new ServerDatabases();
		serverDatabases.add(new Status(200));
		serverDatabases.add(new UpdateType(0));
		
		final Listing listing = itemManager.getDatabases();
		
		serverDatabases.add(new SpecifiedTotalCount(Iterables.size(listing.getListingItems())));
		serverDatabases.add(new ReturnedCount(Iterables.size(listing.getListingItems())));
		
		serverDatabases.add(listing);
		

		// if(request.isUpdateType() && deletedDatabases != null)
		// {
		// DeletedIdListing deletedListing = new DeletedIdListing();
		//
		// for(Database database : deletedDatabases)
		// deletedListing.add(new ItemId(database.getItemId()));
		//
		// serverDatabases.add(deletedListing);
		// }

		return Util.buildResponse(serverDatabases, getDMAPKey(), name);
	}

	@Override
	@Path("databases/{databaseId}/containers")
	@GET
	public Response containers(@PathParam("databaseId") final long databaseId, @QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("meta") final String meta, @QueryParam("hsgid") final String hsgid) throws IOException, SQLException
	{
		//Collection<Container> containers = itemManager.getDatabase(databaseId).getContainers();
		Collection<String> parameters = DmapUtil.parseMeta(meta);

		// iPhoto does not deliver any meta data which means the parameters is empty. Because of this, default parameters are set in
		if(parameters.isEmpty())
		{
			parameters = Lists.newArrayList("dmap.itemkind", "dmap.itemid", "dmap.itemname", "daap.baseplaylist", "dmap.itemcount");
		}
		final Listing listing = itemManager.getContainers(databaseId, parameters);

		final DatabaseContainerns databaseContainers = new DatabaseContainerns();

		databaseContainers.add(new Status(200));
		databaseContainers.add(new UpdateType(0));

/*
		Listing listing = new Listing();
		// iTunes got a bug - if playlists is a empty set, it keeps quering
		for(Container container : containers)
		{
			ListingItem listingItem = new ListingItem();

			for(String key : parameters)
			{
				Chunk chunk = container.getChunk(key);

				if(chunk != null)
				{
					listingItem.add(chunk);
				}
				else
				{
					logger.info("Unknown chunk type: " + key);
				}
			}
			listingItem.add(new ItemCount(container.getMediaItems().size()));
			listing.add(listingItem);
		}*/

		databaseContainers.add(new SpecifiedTotalCount(Iterables.size(listing.getListingItems())));
		databaseContainers.add(new ReturnedCount(Iterables.size(listing.getListingItems())));
		databaseContainers.add(listing);

		// if(request.isUpdateType() && deletedPlaylists != null)
		// {
		// DeletedIdListing deletedListing = new DeletedIdListing();
		//
		// for(Playlist playlist : deletedPlaylists)
		// {
		// deletedListing.add(new ItemId(playlist.getItemId()));
		// }
		//
		// databasePlaylists.add(deletedListing);
		// }

		return Util.buildResponse(databaseContainers, getDMAPKey(), name);
	}

	@Override
	@Path("databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@PathParam("containerId") final long containerId, @PathParam("databaseId") final long databaseId, @QueryParam("session-id") final long sessionId, @QueryParam("revision-number") final long revisionNumber, @QueryParam("delta") final long delta, @QueryParam("meta") final String meta, @QueryParam("type") final String type, @QueryParam("group-type") final String group_type, @QueryParam("sort") final String sort, @QueryParam("include-sort-headers") final String include_sort_headers, @QueryParam("query") final String query, @QueryParam("index") final String index, @QueryParam("hsgid") final String hsgid) throws IOException, SQLException
	{
		// switch on type - for DPAP type is 'photo'
		// dpap:
		// http://192.168.1.2dpap://192.168.1.2:8770/databases/1/containers/5292/items?session-id=1101478641&meta=dpap.aspectratio,dmap.itemid,dmap.itemname,dpap.imagefilename,dpap.imagefilesize,dpap.creationdate,dpap.imagepixelwidth,dpap.imagepixelheight,dpap.imageformat,dpap.imagerating,dpap.imagecomments,dpap.imagelargefilesize&type=photo
		//Container container = itemManager.getDatabase(databaseId).getContainer(containerId);
		// throw new NotImplementedException();
		// /databases/0/containers/1/items?session-id=1570434761&revision-number=2&delta=0&type=music&meta=dmap.itemkind,dmap.itemid,dmap.containeritemid
		final Iterable<String> parameters = DmapUtil.parseMeta(meta);
		final ItemsContainer itemsContainer = new ItemsContainer();

		itemsContainer.add(new Status(200));
		itemsContainer.add(new UpdateType(0));
		
		final Listing listing = itemManager.getMediaItems(databaseId, containerId, parameters);
		
		/*
		Listing listing = new Listing();
		for(MediaItem item : container.getMediaItems())
		{
			ListingItem listingItem = new ListingItem();

			// Added as itemkind is only request in query param which is not yet understood
			listingItem.add(item.getChunk("dmap.itemkind"));

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

			listing.add(listingItem);
		}*/
		
		
		itemsContainer.add(new SpecifiedTotalCount(Iterables.size(listing.getListingItems())));
		itemsContainer.add(new ReturnedCount(Iterables.size(listing.getListingItems())));
		itemsContainer.add(listing);

		// if(request.isUpdateType() && deletedSongs != null)
		// {
		// DeletedIdListing deletedListing = new DeletedIdListing();
		//
		// for(Song song : deletedSongs)
		// {
		// deletedListing.add(song.getChunk("dmap.itemid"));
		// }
		// playlistSongs.add(deletedListing);
		// }
		
		/*
		SortingHeaderListing sortingHeaderListing = new SortingHeaderListing();
		ListingItem item = new ListingItem();
		item.add(new SortingHeaderChar(0x54));//
		item.add(new SortingHeaderIndex(0));
		item.add(new SortingHeaderNumber(listing.size()));
		sortingHeaderListing.add(item);
		itemsContainer.add(sortingHeaderListing);*/
		return Util.buildResponse(itemsContainer, getDMAPKey(), name);
	}

	@Override
	@Path("content-codes")
	@GET
	public Response contentCodes() throws IOException
	{
		return Util.buildResponse(new ContentCodesResponseImpl(), getDMAPKey(), name);
	}

	@Override
	@Path("logout")
	@GET
	public Response logout(@QueryParam("session-id") final long sessionId)
	{
		return Util.buildEmptyResponse(getDMAPKey(), name);
	}
	
	abstract public String getDMAPKey();
}
