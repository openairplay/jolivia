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
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.ardverk.daap.DaapUtil;
import org.ardverk.daap.Database;
import org.ardverk.daap.Playlist;
import org.ardverk.daap.Song;
import org.ardverk.daap.chunks.Chunk;
import org.ardverk.daap.chunks.ContentCodesResponseImpl;
import org.ardverk.daap.chunks.impl.AuthenticationMethod;
import org.ardverk.daap.chunks.impl.AuthenticationSchemes;
import org.ardverk.daap.chunks.impl.ContainerCount;
import org.ardverk.daap.chunks.impl.DaapProtocolVersion;
import org.ardverk.daap.chunks.impl.DatabaseCount;
import org.ardverk.daap.chunks.impl.DatabasePlaylists;
import org.ardverk.daap.chunks.impl.DatabaseSongs;
import org.ardverk.daap.chunks.impl.DmapProtocolVersion;
import org.ardverk.daap.chunks.impl.ItemCount;
import org.ardverk.daap.chunks.impl.ItemId;
import org.ardverk.daap.chunks.impl.ItemName;
import org.ardverk.daap.chunks.impl.Listing;
import org.ardverk.daap.chunks.impl.ListingItem;
import org.ardverk.daap.chunks.impl.LoginRequired;
import org.ardverk.daap.chunks.impl.LoginResponse;
import org.ardverk.daap.chunks.impl.PersistentId;
import org.ardverk.daap.chunks.impl.PlaylistSongs;
import org.ardverk.daap.chunks.impl.ReturnedCount;
import org.ardverk.daap.chunks.impl.ServerDatabases;
import org.ardverk.daap.chunks.impl.ServerInfoResponse;
import org.ardverk.daap.chunks.impl.ServerRevision;
import org.ardverk.daap.chunks.impl.SessionId;
import org.ardverk.daap.chunks.impl.SpecifiedTotalCount;
import org.ardverk.daap.chunks.impl.Status;
import org.ardverk.daap.chunks.impl.SupportsAutoLogout;
import org.ardverk.daap.chunks.impl.SupportsBrowse;
import org.ardverk.daap.chunks.impl.SupportsExtensions;
import org.ardverk.daap.chunks.impl.SupportsIndex;
import org.ardverk.daap.chunks.impl.SupportsPersistentIds;
import org.ardverk.daap.chunks.impl.SupportsQuery;
import org.ardverk.daap.chunks.impl.SupportsUpdate;
import org.ardverk.daap.chunks.impl.TimeoutInterval;
import org.ardverk.daap.chunks.impl.UpdateResponse;
import org.ardverk.daap.chunks.impl.UpdateType;
import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.daap.server.LibraryManager.PasswordMethod;
import org.dyndns.jkiddo.dmap.MDNSResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

@Singleton
public class LibraryResource extends MDNSResource implements ILibraryResource
{
	@Inject
	LibraryManager libraryManager;

	private static final String DAAP_SERVICE_TYPE = "_daap._tcp.local.";
	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DATABASE_ID_KEY = "Database ID";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String MACHINE_NAME_KEY = "Machine Name";
	private static final String ITSH_VERSION_KEY = "iTSh Version";
	private static final String DAAP_VERSION_KEY = "Version";
	private static final String PASSWORD_KEY = "Password";

	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";

	private static final int PARTIAL_CONTENT = 206;

	static Logger logger = LoggerFactory.getLogger(LibraryResource.class);

	@Inject
	public LibraryResource(JmmDNS mDNS, @Named(DAAP_PORT_NAME) Integer port, LibraryManager libraryManager) throws IOException
	{
		super(mDNS, port);
		this.libraryManager = libraryManager;
	}

	@Override
	protected ServiceInfo registerServerRemote()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DATABASE_ID_KEY, hash);
		records.put(MACHINE_ID_KEY, hash);
		records.put(MACHINE_NAME_KEY, hostname);
		records.put(ITSH_VERSION_KEY, DaapUtil.MUSIC_SHARING_VERSION_201 + "");
		records.put(DAAP_VERSION_KEY, DaapUtil.DAAP_VERSION_3 + "");
		records.put(PASSWORD_KEY, "0");
		return ServiceInfo.create(DAAP_SERVICE_TYPE, Jolivia.name, port, 0, 0, records);
	}

	Response buildResponse(Chunk chunk) throws IOException
	{
		return buildResponse().entity(DaapUtil.serialize(chunk, false)).build();// .header("Content-Encoding", "gzip").build();
	}

	Response buildAudioResponse(byte[] buffer, long position, long size)
	{
		ResponseBuilder response = new ResponseBuilderImpl().header("Date", DaapUtil.now()).header("DAAP-Server", libraryManager.getLibraryName()).header("Content-Type", "application/x-dmap-tagged").header("Connection", "close");

		if(position == 0)
		{
			response.status(Response.Status.OK);
			response.header("Content-Length", Long.toString(size));
		}
		else
		{
			response.status(PARTIAL_CONTENT);
			response.header("Content-Length", Long.toString(size - position));
			response.header("Content-Range", "bytes " + position + "-" + (size - 1) + "/" + size);
		}
		response.header("Accept-Ranges", "bytes");
		response.entity(buffer);
		return response.build();
	}

	ResponseBuilder buildResponse()
	{
		return new ResponseBuilderImpl().header("Date", DaapUtil.now()).header("DAAP-Server", libraryManager.getLibraryName()).header("Content-Type", "application/x-dmap-tagged").header("Connection", "Keep-Alive").status(Response.Status.OK);
	}

	Response buildEmptyResponse()
	{
		return buildResponse().status(Response.Status.NO_CONTENT).build();
	}

	@Override
	@Path("/server-info")
	@GET
	public Response serverInfo() throws IOException
	{
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();

		serverInfoResponse.add(new Status(200));
		serverInfoResponse.add(new DmapProtocolVersion(DaapUtil.DMAP_VERSION_201));
		serverInfoResponse.add(new DaapProtocolVersion(DaapUtil.DAAP_VERSION_3));
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

		return buildResponse(serverInfoResponse);
	}

	@Override
	@Path("/login")
	@GET
	public Response login(@Context HttpServletRequest httpServletRequest) throws IOException
	{
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.add(new Status(200));
		loginResponse.add(new SessionId(libraryManager.getSessionId(httpServletRequest.getRemoteHost())));
		return buildResponse(loginResponse);
	}

	@Override
	@Path("/update")
	@GET
	public Response update(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @Context HttpServletRequest httpServletRequest) throws IOException
	{
		if(revisionNumber == delta)
		{
			libraryManager.waitForUpdate();
		}
		UpdateResponse updateResponse = new UpdateResponse();
		updateResponse.add(new Status(200));
		updateResponse.add(new ServerRevision(libraryManager.getRevision(httpServletRequest.getRemoteHost(), sessionId)));
		return buildResponse(updateResponse);
	}

	@Override
	@Path("/databases")
	@GET
	public Response databases(@QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta) throws IOException
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

		return buildResponse(serverDatabases);
	}

	@Override
	@Path("/databases/{databaseId}/items")
	@GET
	public Response songs(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta) throws Exception
	{
		Set<Song> songs = libraryManager.getDatabase(databaseId).getSongs();
		Iterable<String> parameters = DaapUtil.parseMeta(meta);

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

		return buildResponse(databaseSongs);
	}

	@Override
	@Path("/databases/{databaseId}/containers")
	@GET
	public Response playlists(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException
	{
		Collection<Playlist> playlists = libraryManager.getDatabase(databaseId).getPlaylists();
		Iterable<String> parameters = DaapUtil.parseMeta(meta);

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

		return buildResponse(databasePlaylists);
	}

	@Override
	@Path("/databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response playlistSongs(@PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException
	{
		// throw new NotImplementedException();
		// /databases/0/containers/1/items?session-id=1570434761&revision-number=2&delta=0&type=music&meta=dmap.itemkind,dmap.itemid,dmap.containeritemid
		Playlist playlist = libraryManager.getDatabase(databaseId).getPlaylist(containerId);
		Iterable<String> parameters = DaapUtil.parseMeta(meta);
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

		return buildResponse(playlistSongs);
	}

	@Override
	@Path("/content-codes")
	@GET
	public Response contentCodes() throws IOException
	{
		return buildResponse(new ContentCodesResponseImpl());
	}

	@Override
	@Path("/logout")
	@GET
	public Response logout(@QueryParam("session-id") long sessionId)
	{
		return buildEmptyResponse();
	}

	@Override
	@Path("/resolve")
	@GET
	public Response resolve()
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response song(@PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception
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
		return buildAudioResponse(buffer, pos, file.length());
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
	public Response groups(@PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	@Path("/databases/{databaseId}/items/{itemId}/extra_data/artwork")
	@GET
	public Response artwork(@PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("mw") String mw, @QueryParam("mh") String mh) throws Exception
	{
		throw new NotImplementedException();
	}

	// POST /fp-setup?session-id=130493758
	// POST /ript/setpaireddeviceinfo?machine-id=0x0000000000000000 HTTP/1.1
	// POST /ript/fp-setup?fairPlayID=0000000000000000.0000000000000000&fp-make-new=1 HTTP/1.1
	// POST /ript/fp-setup?fairPlayID=0000000000000000.00000000000000000 HTTP/1.1
	// GET /ript/getdeviceprefs?fairPlayID=0000000000000000.00000000000000000 HTTP/1.1
	// http://wiki.awkwardtv.org/wiki/Appletv_Validation
}
