package org.dyndns.jkiddo.service.dmap;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.jmdns.JmmDNS;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmap.Container;
import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.Item;
import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.ContentCodesResponseImpl;
import org.dyndns.jkiddo.dmap.chunks.daap.DatabaseContainerns;
import org.dyndns.jkiddo.dmap.chunks.daap.ServerDatabases;
import org.dyndns.jkiddo.dmap.chunks.dmap.ContainerCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.DatabaseItems;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemId;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemName;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemsContainer;
import org.dyndns.jkiddo.dmap.chunks.dmap.Listing;
import org.dyndns.jkiddo.dmap.chunks.dmap.ListingItem;
import org.dyndns.jkiddo.dmap.chunks.dmap.LoginResponse;
import org.dyndns.jkiddo.dmap.chunks.dmap.PersistentId;
import org.dyndns.jkiddo.dmap.chunks.dmap.ReturnedCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.ServerRevision;
import org.dyndns.jkiddo.dmap.chunks.dmap.SessionId;
import org.dyndns.jkiddo.dmap.chunks.dmap.SpecifiedTotalCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.Status;
import org.dyndns.jkiddo.dmap.chunks.dmap.UpdateResponse;
import org.dyndns.jkiddo.dmap.chunks.dmap.UpdateType;

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
	public Response login(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.add(new Status(200));
		loginResponse.add(new SessionId(itemManager.getSessionId(httpServletRequest.getRemoteHost())));
		return Util.buildResponse(loginResponse, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("update")
	@GET
	public Response update(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("daap-no-disconnect") int daapNoDisconnect) throws IOException
	{
		if(revisionNumber == delta || revisionNumber == itemManager.getRevision(httpServletRequest.getRemoteHost(), sessionId))
		{
			itemManager.waitForUpdate();
		}
		UpdateResponse updateResponse = new UpdateResponse();
		updateResponse.add(new Status(200));
		updateResponse.add(new ServerRevision(itemManager.getRevision(httpServletRequest.getRemoteHost(), sessionId)));
		return Util.buildResponse(updateResponse, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("databases")
	@GET
	public Response databases(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta) throws IOException
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
	public Response containers(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") final long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException
	{
		Collection<Container> containers = itemManager.getDatabase(databaseId).getContainers();
		Iterable<String> parameters = DmapUtil.parseMeta(meta);

		DatabaseContainerns databaseContainers = new DatabaseContainerns();

		databaseContainers.add(new Status(200));
		databaseContainers.add(new UpdateType(0));// Maybe used

		// databasePlaylists.add(new SpecifiedTotalCount(playlists.size()));
		databaseContainers.add(new SpecifiedTotalCount(containers.size()));
		databaseContainers.add(new ReturnedCount(containers.size()));

		Listing listing = new Listing();

		// iTunes got a bug - if playlists is a empty set, it keeps quering
		for(Container playlist : containers)
		{
			ListingItem listingItem = new ListingItem();

			for(String key : parameters)
			{
				Chunk chunk = playlist.getChunk(key);

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
	public Response containerItems(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("containerId") long containerId, @PathParam("databaseId") final long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException
	{
		// dpap:
		// http://192.168.1.2dpap://192.168.1.2:8770/databases/1/containers/5292/items?session-id=1101478641&meta=dpap.aspectratio,dmap.itemid,dmap.itemname,dpap.imagefilename,dpap.imagefilesize,dpap.creationdate,dpap.imagepixelwidth,dpap.imagepixelheight,dpap.imageformat,dpap.imagerating,dpap.imagecomments,dpap.imagelargefilesize&type=photo
		Container container = itemManager.getDatabase(databaseId).getPlaylist(containerId);
		// throw new NotImplementedException();
		// /databases/0/containers/1/items?session-id=1570434761&revision-number=2&delta=0&type=music&meta=dmap.itemkind,dmap.itemid,dmap.containeritemid
		Iterable<String> parameters = DmapUtil.parseMeta(meta);
		ItemsContainer itemsContainer = new ItemsContainer();

		itemsContainer.add(new Status(200));
		// playlistSongs.add(new UpdateType(request.isUpdateType() ? 1 : 0));
		itemsContainer.add(new UpdateType(0));
		itemsContainer.add(new SpecifiedTotalCount(container.getItems().size()));
		itemsContainer.add(new ReturnedCount(container.getItems().size()));

		Listing listing = new Listing();

		for(Item song : container.getItems())
		{
			ListingItem listingItem = new ListingItem();

			for(String key : parameters)
			{
				Chunk chunk = song.getChunk(key);

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

		return Util.buildResponse(itemsContainer, itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("databases/{databaseId}/items")
	@GET
	public Response items(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") final long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta, @QueryParam("query") String query) throws Exception
	{
		// dpap: limited by query
		// http://192.168.1.2dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.thumb,dmap.itemid,dpap.filedata&query=('dmap.itemid:2810','dmap.itemid:2811','dmap.itemid:2812','dmap.itemid:2813','dmap.itemid:2814','dmap.itemid:2815','dmap.itemid:2816','dmap.itemid:2817','dmap.itemid:2818','dmap.itemid:2819','dmap.itemid:2820','dmap.itemid:2821','dmap.itemid:2822','dmap.itemid:2823','dmap.itemid:2824','dmap.itemid:2825','dmap.itemid:2826','dmap.itemid:2827','dmap.itemid:2851','dmap.itemid:2852')

		// .getDatabases(), new Predicate<Database>() {
		// @Override
		// public boolean apply(Database database)
		// {
		// return database.getItemId() == databaseId;
		// }
		// }).getItems();
		Set<Item> items = itemManager.getDatabase(databaseId).getItems();

		Iterable<String> parameters = DmapUtil.parseMeta(meta);

		DatabaseItems databaseSongs = new DatabaseItems();

		databaseSongs.add(new Status(200));
		databaseSongs.add(new UpdateType(0));
		databaseSongs.add(new SpecifiedTotalCount(items.size()));

		databaseSongs.add(new ReturnedCount(items.size()));

		Listing listing = new Listing();
		for(Item item : items)
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
	@Path("content-codes")
	@GET
	public Response contentCodes(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		return Util.buildResponse(new ContentCodesResponseImpl(), itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("logout")
	@GET
	public Response logout(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId)
	{
		return Util.buildEmptyResponse(itemManager.getDMAPKey(), name);
	}

	@Override
	@Path("databases/{databaseId}/groups")
	@GET
	public Response groups(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws Exception
	{
		throw new NotImplementedException();
	}
}
