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
package org.dyndns.jkiddo.daap.server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import javax.inject.Inject;
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
import org.dyndns.jkiddo.daap.server.MusicLibraryManager.PasswordMethod;
import org.dyndns.jkiddo.dmap.service.MDNSResource;
import org.dyndns.jkiddo.dmap.service.Util;
import org.dyndns.jkiddo.guice.JoliviaListener;
import org.dyndns.jkiddo.protocol.dmap.Database;
import org.dyndns.jkiddo.protocol.dmap.DmapUtil;
import org.dyndns.jkiddo.protocol.dmap.Playlist;
import org.dyndns.jkiddo.protocol.dmap.Song;
import org.dyndns.jkiddo.protocol.dmap.chunks.Chunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.ContentCodesResponseImpl;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.DaapProtocolVersion;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.DatabasePlaylists;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.DatabaseSongs;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.PlaylistSongs;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.ServerDatabases;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.AuthenticationMethod;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.AuthenticationSchemes;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ContainerCount;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.DatabaseCount;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.DmapProtocolVersion;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemCount;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemId;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemName;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.Listing;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ListingItem;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.LoginRequired;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.LoginResponse;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.PersistentId;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ReturnedCount;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ServerInfoResponse;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ServerRevision;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SessionId;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SpecifiedTotalCount;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.Status;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SupportsAutoLogout;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SupportsBrowse;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SupportsExtensions;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SupportsIndex;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SupportsPersistentIds;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SupportsQuery;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SupportsUpdate;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.TimeoutInterval;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.UpdateResponse;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.UpdateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class MusicLibraryResource extends MDNSResource implements IMusicLibrary
{
	private final MusicLibraryManager libraryManager;

	private final String name;

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DATABASE_ID_KEY = "Database ID";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String MACHINE_NAME_KEY = "Machine Name";
	private static final String ITSH_VERSION_KEY = "iTSh Version";
	private static final String DAAP_VERSION_KEY = "Version";
	private static final String PASSWORD_KEY = "Password";

	private static final String DMAP_KEY = "DAAP-Server";

	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";

	static Logger logger = LoggerFactory.getLogger(MusicLibraryResource.class);

	@Inject
	public MusicLibraryResource(JmmDNS mDNS, @Named(DAAP_PORT_NAME) Integer port, MusicLibraryManager libraryManager, @Named(JoliviaListener.APPLICATION_NAME) String applicationName) throws IOException
	{
		super(mDNS, port);
		this.libraryManager = libraryManager;
		this.name = applicationName;
		this.signUp();	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DATABASE_ID_KEY, hash);
		records.put(MACHINE_ID_KEY, hash);
		records.put(MACHINE_NAME_KEY, hostname);
		records.put(ITSH_VERSION_KEY, DmapUtil.MUSIC_SHARING_VERSION_201 + "");
		records.put(DAAP_VERSION_KEY, DmapUtil.DAAP_VERSION_3 + "");
		records.put(PASSWORD_KEY, "0");
		return ServiceInfo.create(DAAP_SERVICE_TYPE, name, port, 0, 0, records);
	}

	@Override
	@Path("/server-info")
	@GET
	public Response serverInfo(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();

		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(new DmapProtocolVersion(DmapUtil.DMAP_VERSION_201));
		serverInfoResponse.add(new DaapProtocolVersion(DmapUtil.DAAP_VERSION_3));
		serverInfoResponse.add(new ItemName(libraryManager.getLibraryName()));
		serverInfoResponse.add(new TimeoutInterval(1800));
		serverInfoResponse.add(new SupportsAutoLogout(true));

		// serverInfoResponse.add(new
		// MusicSharingVersion(DaapUtil.MUSIC_SHARING_VERSION_201));

		// NOTE: the value of the following boolean chunks does not matter!
		// They are either present (=true) or not (=false).

		// client should perform /login request (create session)

		serverInfoResponse.add(new LoginRequired(true));
		serverInfoResponse.add(new AuthenticationMethod(AuthenticationMethod.NONE));
		serverInfoResponse.add(new SupportsExtensions(true));
		serverInfoResponse.add(new SupportsIndex(true));
		serverInfoResponse.add(new SupportsBrowse(true));
		serverInfoResponse.add(new SupportsQuery(true));
		serverInfoResponse.add(new SupportsPersistentIds(true));

		PasswordMethod authenticationMethod = libraryManager.getAuthenticationMethod();
		if(!authenticationMethod.equals(PasswordMethod.NO_PASSWORD))
		{
			if(authenticationMethod.equals(PasswordMethod.PASSWORD))
			{
				serverInfoResponse.add(new AuthenticationMethod(AuthenticationMethod.PASSWORD_METHOD));
			}
			else
			{
				serverInfoResponse.add(new AuthenticationMethod(AuthenticationMethod.USERNAME_PASSWORD_METHOD));
			}

			serverInfoResponse.add(new AuthenticationSchemes(AuthenticationSchemes.BASIC_SCHEME | AuthenticationSchemes.DIGEST_SCHEME));
		}

		serverInfoResponse.add(new DatabaseCount(libraryManager.getNrOfDatabases()));
		serverInfoResponse.add(new SupportsUpdate(true));

		return Util.buildResponse(serverInfoResponse, DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/login")
	@GET
	public Response login(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.add(new Status(200));
		loginResponse.add(new SessionId(libraryManager.getSessionId(httpServletRequest.getRemoteHost())));
		return Util.buildResponse(loginResponse, DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/update")
	@GET
	public Response update(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("daap-no-disconnect") int daapNoDisconnect) throws IOException
	{
		if(revisionNumber == delta)
		{
			libraryManager.waitForUpdate();
		}
		UpdateResponse updateResponse = new UpdateResponse();
		updateResponse.add(new Status(200));
		updateResponse.add(new ServerRevision(libraryManager.getRevision(httpServletRequest.getRemoteHost(), sessionId)));
		return Util.buildResponse(updateResponse, DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/databases")
	@GET
	public Response databases(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta) throws IOException
	{
		ServerDatabases serverDatabases = new ServerDatabases();

		serverDatabases.add(new Status(200));
		serverDatabases.add(new UpdateType(0));
		serverDatabases.add(new SpecifiedTotalCount(libraryManager.getNrOfDatabases()));

		serverDatabases.add(new ReturnedCount(libraryManager.getNrOfDatabases()));

		Listing listing = new Listing();

		Collection<Database> databases = libraryManager.getDatabases();

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

		return Util.buildResponse(serverDatabases, DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/databases/{databaseId}/items")
	@GET
	public Response items(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta) throws Exception
	{
		Set<Song> songs = libraryManager.getDatabase(databaseId).getSongs();
		Iterable<String> parameters = DmapUtil.parseMeta(meta);

		DatabaseSongs databaseSongs = new DatabaseSongs();

		databaseSongs.add(new Status(200));
		// databaseSongs.add(new UpdateType(request.isUpdateType() ? 1 : 0));
		databaseSongs.add(new UpdateType(0)); // Maybe?
		databaseSongs.add(new SpecifiedTotalCount(songs.size()));

		databaseSongs.add(new ReturnedCount(songs.size()));

		Listing listing = new Listing();
		for(Song song : songs)
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

		return Util.buildResponse(databaseSongs, DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/databases/{databaseId}/containers")
	@GET
	public Response containers(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException
	{
		Collection<Playlist> playlists = libraryManager.getDatabase(databaseId).getPlaylists();
		Iterable<String> parameters = DmapUtil.parseMeta(meta);

		DatabasePlaylists databasePlaylists = new DatabasePlaylists();

		databasePlaylists.add(new Status(200));
		databasePlaylists.add(new UpdateType(0));// Maybe used

		// databasePlaylists.add(new SpecifiedTotalCount(playlists.size()));
		databasePlaylists.add(new SpecifiedTotalCount(0));
		databasePlaylists.add(new ReturnedCount(playlists.size()));

		Listing listing = new Listing();

		// iTunes got a bug - if playlists is a empty set, it keeps quering
		for(Playlist playlist : playlists)
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

		return Util.buildResponse(databasePlaylists, DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException
	{
		// throw new NotImplementedException();
		// /databases/0/containers/1/items?session-id=1570434761&revision-number=2&delta=0&type=music&meta=dmap.itemkind,dmap.itemid,dmap.containeritemid
		Playlist playlist = libraryManager.getDatabase(databaseId).getPlaylist(containerId);
		Iterable<String> parameters = DmapUtil.parseMeta(meta);
		PlaylistSongs playlistSongs = new PlaylistSongs();

		playlistSongs.add(new Status(200));
		// playlistSongs.add(new UpdateType(request.isUpdateType() ? 1 : 0));
		playlistSongs.add(new UpdateType(0));
		playlistSongs.add(new SpecifiedTotalCount(playlist.getSongs().size()));
		playlistSongs.add(new ReturnedCount(playlist.getSongs().size()));

		Listing listing = new Listing();

		for(Song song : playlist.getSongs())
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
		}

		playlistSongs.add(listing);

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

		return Util.buildResponse(playlistSongs, DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/content-codes")
	@GET
	public Response contentCodes(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException
	{
		return Util.buildResponse(new ContentCodesResponseImpl(), DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/logout")
	@GET
	public Response logout(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId)
	{
		return Util.buildEmptyResponse(DMAP_KEY, libraryManager.getLibraryName());
	}

	@Override
	@Path("/resolve")
	@GET
	public Response resolve(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse)
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception
	{
		File file = libraryManager.getTune(databaseId, itemId);

		long[] range = getRange(rangeHeader, 0, file.length());
		int pos = (int) range[0];
		int end = (int) range[1];
		RandomAccessFile raf = new RandomAccessFile(file, "r");

		byte[] buffer = new byte[end - pos];
		raf.seek(pos);
		raf.readFully(buffer, 0, buffer.length);
		Closeables.closeQuietly(raf);
		return Util.buildAudioResponse(buffer, pos, file.length(), DMAP_KEY, libraryManager.getLibraryName());
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
	@Path("/databases/{databaseId}/groups")
	@GET
	public Response groups(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}/extra_data/artwork")
	@GET
	public Response artwork(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("mw") String mw, @QueryParam("mh") String mh) throws Exception
	{
		throw new NotImplementedException();
	}

	// POST /ript/setpaireddeviceinfo?machine-id=0x0000000000000000 HTTP/1.1
	// POST /ript/fp-setup?fairPlayID=0000000000000000.0000000000000000&fp-make-new=1 HTTP/1.1
	// POST /ript/fp-setup?fairPlayID=0000000000000000.00000000000000000 HTTP/1.1
	// GET /ript/getdeviceprefs?fairPlayID=0000000000000000.00000000000000000 HTTP/1.1
	// http://wiki.awkwardtv.org/wiki/Appletv_Validation
}
