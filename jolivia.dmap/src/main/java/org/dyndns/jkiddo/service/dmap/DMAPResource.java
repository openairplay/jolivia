package org.dyndns.jkiddo.service.dmap;

import java.io.IOException;
import java.util.Collection;

import javax.jmdns.JmmDNS;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.dmap.Container;
import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.MediaItem;
import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.ContentCodesResponseImpl;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseContainerns;
import org.dyndns.jkiddo.dmap.chunks.audio.ItemsContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.ServerDatabases;
import org.dyndns.jkiddo.dmap.chunks.media.ContainerCount;
import org.dyndns.jkiddo.dmap.chunks.media.ItemCount;
import org.dyndns.jkiddo.dmap.chunks.media.ItemId;
import org.dyndns.jkiddo.dmap.chunks.media.ItemName;
import org.dyndns.jkiddo.dmap.chunks.media.Listing;
import org.dyndns.jkiddo.dmap.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmap.chunks.media.LoginResponse;
import org.dyndns.jkiddo.dmap.chunks.media.PersistentId;
import org.dyndns.jkiddo.dmap.chunks.media.ReturnedCount;
import org.dyndns.jkiddo.dmap.chunks.media.ServerRevision;
import org.dyndns.jkiddo.dmap.chunks.media.SessionId;
import org.dyndns.jkiddo.dmap.chunks.media.SpecifiedTotalCount;
import org.dyndns.jkiddo.dmap.chunks.media.Status;
import org.dyndns.jkiddo.dmap.chunks.media.UnknownHC;
import org.dyndns.jkiddo.dmap.chunks.media.UnknownHI;
import org.dyndns.jkiddo.dmap.chunks.media.UnknownHL;
import org.dyndns.jkiddo.dmap.chunks.media.UnknownHN;
import org.dyndns.jkiddo.dmap.chunks.media.UpdateResponse;
import org.dyndns.jkiddo.dmap.chunks.media.UpdateType;

@Consumes(MediaType.WILDCARD)
@Produces(MediaType.WILDCARD)
public abstract class DMAPResource<T extends IItemManager> extends MDNSResource implements ILibraryResource
{
	final protected T itemManager;
	protected String name;

	public DMAPResource(JmmDNS mDNS, Integer port, T itemManager) throws IOException
	{
		super(mDNS, port);
		this.itemManager = itemManager;
	}

	@Override
	@Path("login")
	@GET
	public Response login(@QueryParam("pairing-guid") String guid, @QueryParam("hasFP") int value, @QueryParam("hsgid") String hsgid) throws IOException
	{
		String s = Thread.currentThread().getId() + "";
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.add(new Status(200));
		loginResponse.add(new SessionId(itemManager.getSessionId(s)));
		return Util.buildResponse(loginResponse, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("update")
	@GET
	public Response update(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("daap-no-disconnect") int daapNoDisconnect, @QueryParam("hsgid") String hsgid) throws IOException
	{
		String s = Thread.currentThread().getId() + "";
		if(revisionNumber == delta || revisionNumber == itemManager.getRevision(s, sessionId))
		{
			itemManager.waitForUpdate();
		}
		UpdateResponse updateResponse = new UpdateResponse();
		updateResponse.add(new Status(200));
		updateResponse.add(new ServerRevision(itemManager.getRevision(s, sessionId)));
		return Util.buildResponse(updateResponse, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("databases")
	@GET
	public Response databases(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("hsgid") String hsgid) throws IOException
	{
		ServerDatabases serverDatabases = new ServerDatabases();
		serverDatabases.add(new Status(200));
		serverDatabases.add(new UpdateType(0));
		serverDatabases.add(new SpecifiedTotalCount(itemManager.getDatabases().size()));

		serverDatabases.add(new ReturnedCount(itemManager.getDatabases().size()));

		Listing listing = new Listing();

		Collection<Database> databases = itemManager.getDatabases();

		for(Database database : databases)
		{
			ListingItem listingItem = new ListingItem();
			listingItem.add(new ItemId(database.getItemId()));
			listingItem.add(new PersistentId(database.getPersistentId()));
			listingItem.add(new ItemName(database.getName()));
			listingItem.add(new ItemCount(database.getSongCount()));
			listingItem.add(new ContainerCount(database.getPlaylistCount()));

			listing.add(listingItem);
		}

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

		return Util.buildResponse(serverDatabases, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("databases/{databaseId}/containers")
	@GET
	public Response containers(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("hsgid") String hsgid) throws IOException
	{
		Collection<Container> containers = itemManager.getDatabase(databaseId).getContainers();
		Iterable<String> parameters = DmapUtil.parseMeta(meta);

		DatabaseContainerns databaseContainers = new DatabaseContainerns();

		databaseContainers.add(new Status(200));
		databaseContainers.add(new UpdateType(0));

		// databasePlaylists.add(new SpecifiedTotalCount(playlists.size()));
		databaseContainers.add(new SpecifiedTotalCount(containers.size()));
		databaseContainers.add(new ReturnedCount(containers.size()));

		Listing listing = new Listing();

		// iTunes got a bug - if playlists is a empty set, it keeps quering
		for(Container container : containers)
		{
			ListingItem listingItem = new ListingItem();

			// iPhoto
			if(!parameters.iterator().hasNext())
			{
				listingItem.add(container.getChunk("dmap.itemkind"));
				listingItem.add(container.getChunk("dmap.itemid"));
				listingItem.add(container.getChunk("dmap.itemname"));
				listingItem.add(container.getChunk("daap.baseplaylist"));
				listingItem.add(container.getChunk("dmap.itemcount"));
			}

			// iTunes
			else
			{
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
				listingItem.add(new ItemCount(1));
			}
			listing.add(listingItem);
		}

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

		return Util.buildResponse(databaseContainers, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index, @QueryParam("hsgid") String hsgid) throws IOException
	{
		// switch on type - for DPAP type is 'photo'
		// dpap:
		// http://192.168.1.2dpap://192.168.1.2:8770/databases/1/containers/5292/items?session-id=1101478641&meta=dpap.aspectratio,dmap.itemid,dmap.itemname,dpap.imagefilename,dpap.imagefilesize,dpap.creationdate,dpap.imagepixelwidth,dpap.imagepixelheight,dpap.imageformat,dpap.imagerating,dpap.imagecomments,dpap.imagelargefilesize&type=photo
		Container container = itemManager.getDatabase(databaseId).getContainer(containerId);
		// throw new NotImplementedException();
		// /databases/0/containers/1/items?session-id=1570434761&revision-number=2&delta=0&type=music&meta=dmap.itemkind,dmap.itemid,dmap.containeritemid
		Iterable<String> parameters = DmapUtil.parseMeta(meta);
		ItemsContainer itemsContainer = new ItemsContainer();

		itemsContainer.add(new Status(200));
		itemsContainer.add(new UpdateType(0));
		itemsContainer.add(new SpecifiedTotalCount(container.getItems().size()));
		itemsContainer.add(new ReturnedCount(container.getItems().size()));

		Listing listing = new Listing();

		for(MediaItem item : container.getItems())
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
		}

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
		UnknownHL unknownHl = new UnknownHL();
		ListingItem item = new ListingItem();
		item.add(new UnknownHC(0x54));//
		item.add(new UnknownHI(0));
		item.add(new UnknownHN(listing.size()));
		unknownHl.add(item);
		itemsContainer.add(unknownHl);
		return Util.buildResponse(itemsContainer, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("content-codes")
	@GET
	public Response contentCodes() throws IOException
	{
		return Util.buildResponse(new ContentCodesResponseImpl(), itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("logout")
	@GET
	public Response logout(@QueryParam("session-id") long sessionId)
	{
		return Util.buildEmptyResponse(itemManager.getDMAPKey(), name);
	}
}
