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
package org.dyndns.jkiddo.service.dpap.server;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.Item;
import org.dyndns.jkiddo.dmap.Container;
import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.daap.DaapProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.daap.DatabasePlaylists;
import org.dyndns.jkiddo.dmap.chunks.daap.ServerDatabases;
import org.dyndns.jkiddo.dmap.chunks.dmap.ContainerCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.DatabaseCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.DatabaseItems;
import org.dyndns.jkiddo.dmap.chunks.dmap.DmapProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemId;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemName;
import org.dyndns.jkiddo.dmap.chunks.dmap.Listing;
import org.dyndns.jkiddo.dmap.chunks.dmap.ListingItem;
import org.dyndns.jkiddo.dmap.chunks.dmap.LoginRequired;
import org.dyndns.jkiddo.dmap.chunks.dmap.LoginResponse;
import org.dyndns.jkiddo.dmap.chunks.dmap.ItemsContainer;
import org.dyndns.jkiddo.dmap.chunks.dmap.ReturnedCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.ServerInfoResponse;
import org.dyndns.jkiddo.dmap.chunks.dmap.SessionId;
import org.dyndns.jkiddo.dmap.chunks.dmap.SpecifiedTotalCount;
import org.dyndns.jkiddo.dmap.chunks.dmap.Status;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsAutoLogout;
import org.dyndns.jkiddo.dmap.chunks.dmap.SupportsIndex;
import org.dyndns.jkiddo.dmap.chunks.dmap.TimeoutInterval;
import org.dyndns.jkiddo.dmap.chunks.dmap.UpdateType;
import org.dyndns.jkiddo.guice.JoliviaListener;
import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ImageResource extends MDNSResource implements IImageLibrary
{
	public static final String DPAP_SERVER_PORT_NAME = "DPAP_SERVER_PORT_NAME";

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DPAP_VERSION_KEY = "Version";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String IPSH_VERSION_KEY = "iPSh Version";
	private static final String PASSWORD_KEY = "Password";
	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";

	private static final String DMAP_KEY = "DPAP-Server";

	private final String name;

	private final ImageLibraryManager imageLibraryManager;

	@Inject
	public ImageResource(JmmDNS mDNS, @Named(DPAP_SERVER_PORT_NAME) Integer port, @Named(JoliviaListener.APPLICATION_NAME) String applicationName, ImageLibraryManager imageLibraryManager) throws IOException
	{
		super(mDNS, port);
		this.name = applicationName;
		this.signUp();
		this.imageLibraryManager = imageLibraryManager;
	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DPAP_VERSION_KEY, DmapUtil.DPAP_VERSION_1 + "");
		records.put(IPSH_VERSION_KEY, 0x20000 + "");
		records.put(MACHINE_ID_KEY, hash);
		records.put(PASSWORD_KEY, "0");
		return ServiceInfo.create(DPAP_SERVICE_TYPE, name, port, 0, 0, records);
	}

	@Override
	@Path("/server-info")
	@GET
	public Response serverInfo(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(new DmapProtocolVersion(DmapUtil.DMAP_VERSION_420));
		serverInfoResponse.add(new DaapProtocolVersion(DmapUtil.DPAP_VERSION_411));
		serverInfoResponse.add(new ItemName(imageLibraryManager.getLibraryName()));
		serverInfoResponse.add(new LoginRequired(true));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(true));
		serverInfoResponse.add(new SupportsIndex(true));
		serverInfoResponse.add(new DatabaseCount(imageLibraryManager.getNrOfDatabases()));

		return Util.buildResponse(serverInfoResponse, DMAP_KEY, "Some name");
	}
	@Override
	@Path("/login")
	@GET
	public Response login(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.add(new Status(200));
		loginResponse.add(new SessionId(imageLibraryManager.getSessionId(httpServletRequest.getRemoteHost())));
		return Util.buildResponse(loginResponse, DMAP_KEY, imageLibraryManager.getLibraryName());
	}

	@Override
	@Path("/update")
	@GET
	public Response update(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("daap-no-disconnect") int daapNoDisconnect) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases")
	@GET
	public Response databases(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta) throws IOException
	{
		ServerDatabases serverDatabases = new ServerDatabases();

		serverDatabases.add(new Status(200));
		serverDatabases.add(new UpdateType(0));
		serverDatabases.add(new SpecifiedTotalCount(imageLibraryManager.getNrOfDatabases()));

		serverDatabases.add(new ReturnedCount(imageLibraryManager.getNrOfDatabases()));

		Listing listing = new Listing();

		Collection<Database> databases = imageLibraryManager.getDatabases();

		for(Database database : databases)
		{
			ListingItem listingItem = new ListingItem();
			listingItem.add(new ItemId(database.getItemId()));
			// listingItem.add(new PersistentId(database.getPersistentId()));
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

		return Util.buildResponse(serverDatabases, DMAP_KEY, imageLibraryManager.getLibraryName());
	}
	@Override
	@Path("/databases/{databaseId}/groups")
	@GET
	public Response groups(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/items")
	@GET
	public Response items(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta, @QueryParam("query") String query) throws Exception
	{
		// Picture request
		// GET dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.hires,dmap.itemid,dpap.filedata&query=('dmap.itemid:2742') HTTP/1.1
		//
		// 5487
		// HTTP/1.1 200 OK
		// Date: Fri, 18 Jan 2013 18:57:33 GMT
		// DPAP-Server: iPhoto/9.2.1 (Mac OS X)
		// Content-Type: application/x-dmap-tagged
		// Content-Length: 6510908

		//Number of listings should be returned according to query string
		
		Set<Item> images = imageLibraryManager.getDatabase(databaseId).getItems();
		Iterable<String> parameters = DmapUtil.parseMeta(meta);

		DatabaseItems databaseImages = new DatabaseItems();
		databaseImages.add(new Status(200));
		databaseImages.add(new UpdateType(0));
		databaseImages.add(new SpecifiedTotalCount(images.size()));
		databaseImages.add(new ReturnedCount(images.size()));

		Listing listing = new Listing();
		for(Item item : images)
		{
			ListingItem listingItem = new ListingItem();

			for(String key : parameters)
			{
				Chunk chunk = item.getChunk(key);

				if(chunk != null)
				{
					listingItem.add(chunk);
				}
				else
				{
					logger.debug("Unknown chunk type: " + key);
				}
			}

			// TODO Remember to add FileData/pfdt - maybe resized

			listing.add(listingItem);
		}

		databaseImages.add(listing);

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

		return Util.buildResponse(databaseImages, DMAP_KEY, imageLibraryManager.getLibraryName());
	}

	@Override
	@Path("/databases/{databaseId}/containers")
	@GET
	public Response containers(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException
	{
		// See how it is done in MusicLibraryResource
		// aply
		// mstt
		// muty
		// mtco
		// mrco
		// mlcl
		// mlitL
		// mikd
		// miid
		// minmSome Name bibliotek
		// abpl
		// mimc
		// mlit3
		// mikd
		// miid
		// minm03/04/2007
		// mimc
		// mlit4
		// mikd
		// miid
		// minmLast Import
		// mimc
		// mlit7
		// mikd
		// miid
		// minm
		// Last 12 Monthsmimcmlit2mikdmiidminm tom mappemimcmlit:mikdmiidminmAlbum uden navn 3mimcmlit4mikdmiid9minmFamiliefestmimcmlit?mikdmiidminmEmner til fremkaldelsemimc0mlit:mikdmiid%minmAlbum uden navn 2mimcmlit0mikdmiidminmBettinamimcmlit1mikdmiidminmKalendermimc1mlit2mikdmiid!minm Fastelavnmimcmlit3mikdmiidminm
		// Kbenhavnmimcmlit0mikdmiidminmFlaggedmimcmlit.mikdmiidminmTrashmimc

		Collection<Container> playlists = imageLibraryManager.getDatabase(databaseId).getPlaylists();
		Iterable<String> parameters = DmapUtil.parseMeta(meta);

		DatabasePlaylists databasePlaylists = new DatabasePlaylists();

		databasePlaylists.add(new Status(200));
		databasePlaylists.add(new UpdateType(0));// Maybe used

		databasePlaylists.add(new SpecifiedTotalCount(playlists.size()));
		// databasePlaylists.add(new SpecifiedTotalCount(0));
		databasePlaylists.add(new ReturnedCount(playlists.size()));

		Listing listing = new Listing();

		// iTunes got a bug - if playlists is a empty set, it keeps quering
		for(Container playlist : playlists)
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
					logger.debug("Unknown chunk type: " + key);
				}
			}

			listing.add(listingItem);

			// TODO if playlist is a 'base playlist', remember to add BasePlaylist/abpl
		}

		databasePlaylists.add(listing);

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

		return Util.buildResponse(databasePlaylists, DMAP_KEY, imageLibraryManager.getLibraryName());
	}

	@Override
	@Path("/databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException
	{
		// See how it is done in MusicLibraryResource
		// apso)
		// mstt
		// muty
		// mtco
		// mrco
		// mlcl)z
		// mlit
		// mikd
		// pasp1.5
		// picd8m5p
		// miid
		// pimfIMG_0041.JPG
		// minmIMG_0041.JPG
		// pifsf[
		// pwth
		// phgt
		// pfmtJPEG
		// prat
		// plszf[
		// mlitmikdpasp1.5picd8m5pmiid
		// pimfIMG_0043.JPGminmIMG_0043.JPGpifsuYpwthphgt pfmtJPEGpratplszuYmlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0026.JPGminmIMG_0026.JPGpifsJpwthphgt pfmtJPEGpratplszJmlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0039.JPGminmIMG_0039.JPGpifs]3pwthphgt pfmtJPEGpratplsz]3mlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0047.JPGminmIMG_0047.JPGpifsnpwthphgt pfmtJPEGpratplsznmlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0065.JPGminmIMG_0065.JPGpifs`pwthphgt pfmtJPEGpratplsz`mlitmikdpasp1.5picd8m5qmiid
		// pimfIMG_0077.JPGminmIMG_0077.JPGpifs?"pwthphgt pfmtJPEGpratplsz?"mlitmikdpasp1.5picd8m5rmiid
		// pimfIMG_0045.JPGminmIMG_0045.JPGpifs`\pwthphgt pfmtJPEGpratplsz`\mlitmikdpasp1.5picd8m5rm
		// throw new NotImplementedException();
		// /databases/0/containers/1/items?session-id=1570434761&revision-number=2&delta=0&type=music&meta=dmap.itemkind,dmap.itemid,dmap.containeritemid
		Container container = imageLibraryManager.getDatabase(databaseId).getPlaylist(containerId);
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
					logger.debug("Unknown chunk type: " + key);
				}
			}

			listing.add(listingItem);

			// TODO
			// Each listing should contain the following:
			// pasp1.5
			// picd8m5p
			// miid
			// pimfIMG_0041.JPG
			// minmIMG_0041.JPG
			// pifsf[
			// pwth
			// phgt
			// pfmtJPEG
			// prat
			// plszf[
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

		return Util.buildResponse(itemsContainer, DMAP_KEY, imageLibraryManager.getLibraryName());
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/content-codes")
	@GET
	public Response contentCodes(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/logout")
	@GET
	public Response logout(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId)
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/resolve")
	@GET
	public Response resolve(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse)
	{
		throw new NotImplementedException();
	}
}
