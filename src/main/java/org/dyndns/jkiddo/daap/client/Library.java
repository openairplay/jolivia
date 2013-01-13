/*
    TunesRemote+ - http://code.google.com/p/tunesremote-plus/
    
    Copyright (C) 2008 Jeffrey Sharkey, http://jsharkey.org/
    Copyright (C) 2010 TunesRemote+, http://code.google.com/p/tunesremote-plus/
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    The Initial Developer of the Original Code is Jeffrey Sharkey.
    Portions created by Jeffrey Sharkey are
    Copyright (C) 2008. Jeffrey Sharkey, http://jsharkey.org/
    All Rights Reserved.
 */

package org.dyndns.jkiddo.daap.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.ardverk.daap.chunks.impl.daap.DatabaseBrowse;
import org.ardverk.daap.chunks.impl.daap.PlaylistSongs;
import org.ardverk.daap.chunks.impl.daap.SongAlbumId;
import org.ardverk.daap.chunks.impl.dacp.FullscreenStatus;
import org.ardverk.daap.chunks.impl.dacp.GeniusSelectable;
import org.ardverk.daap.chunks.impl.dacp.NowPlaying;
import org.ardverk.daap.chunks.impl.dacp.PlayStatus;
import org.ardverk.daap.chunks.impl.dacp.ProgressRemain;
import org.ardverk.daap.chunks.impl.dacp.ProgressTotal;
import org.ardverk.daap.chunks.impl.dacp.RepeatStatus;
import org.ardverk.daap.chunks.impl.dacp.ShuffleStatus;
import org.ardverk.daap.chunks.impl.dacp.StatusRevision;
import org.ardverk.daap.chunks.impl.dacp.TrackAlbum;
import org.ardverk.daap.chunks.impl.dacp.TrackArtist;
import org.ardverk.daap.chunks.impl.dacp.TrackGenre;
import org.ardverk.daap.chunks.impl.dacp.TrackName;
import org.ardverk.daap.chunks.impl.dacp.UnknownAL;
import org.ardverk.daap.chunks.impl.dacp.UnknownST;
import org.ardverk.daap.chunks.impl.dacp.VisualizerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

public class Library
{

	public final static Logger logger = LoggerFactory.getLogger(Library.class);

	final Session session;

	public Library(Session session)
	{
		this.session = session;
	}

	/**
	 * Performs a search of the DACP Server sending it search criteria and an index of how many items to find.
	 * <p>
	 * 
	 * @param listener
	 *            the TagListener to capture records coming in for the UI
	 * @param search
	 *            the search criteria
	 * @param start
	 *            items to start with for paging (usually 0)
	 * @param items
	 *            the total items to return in this search
	 * @return the count of records returned or -1 if nothing found
	 * @throws Exception
	 */
	public long readSearch(String search, long start, long items) throws Exception
	{
		final String encodedSearch = Library.escapeUrlString(search);
		final PlaylistSongs playlistSongs = RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=name&include-sort-headers=1&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+('dmap.itemname:*%s*','daap.songartist:*%s*','daap.songalbum:*%s*'))&index=%d-%d", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), encodedSearch, encodedSearch, encodedSearch, start, items), false);
		return playlistSongs.getSpecifiedTotalCount().getValue();
	}

	public int readArtists() throws Exception
	{
		// request ALL artists for performance
		// /databases/%d/browse/artists?session-id=%s&include-sort-headers=1&index=%d-%d
		DatabaseBrowse databaseBrowse = RequestHelper.requestParsed(String.format("%s/databases/%d/browse/artists?session-id=%s&include-sort-headers=1", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId()), false, true);
		return Iterables.size(databaseBrowse.getBrowseArtistListing().getSongArtists());
	}

	public int readAlbums(String artist) throws Exception
	{
		final String encodedArtist = Library.escapeUrlString(artist);
		// make albums request for this artist
		// http://192.168.254.128:3689/databases/36/groups?session-id=1034286700&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1

		UnknownAL unknownAL = RequestHelper.requestParsed(String.format("%s/databases/%d/groups?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1&query='daap.songartist:%s'", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId(), encodedArtist), false);
		return Iterables.size(unknownAL.getListing().getListingItems());
	}

	public int readAlbums() throws Exception
	{
		// make partial album list request
		// http://192.168.254.128:3689/databases/36/groups?session-id=1034286700&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1&index=0-50
		UnknownAL unknownAL = RequestHelper.requestParsed(String.format("%s/databases/%d/groups?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=album&include-sort-headers=1", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId()), false);
		return Iterables.size(unknownAL.getListing().getListingItems());
	}

	public int readAllTracks() throws Exception
	{
		// make tracks list request
		// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
		PlaylistSongs playlistSongs = RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId()), false);
		return Iterables.size(playlistSongs.getListing().getListingItems());
	}

	public int readTracks(String albumid) throws Exception
	{
		// make tracks list request
		// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
		PlaylistSongs playlistSongs = RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:%s'", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), albumid), false);
		return Iterables.size(playlistSongs.getListing().getListingItems());
	}

	public int readAllTracks(String artist) throws Exception
	{
		final String encodedArtist = Library.escapeUrlString(artist);

		// make tracks list request
		// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
		PlaylistSongs playlistSongs = RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songartist:%s'", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), encodedArtist), false);
		return Iterables.size(playlistSongs.getListing().getListingItems());
	}

	public int readPlaylist(String playlistid) throws Exception
	{
		// http://192.168.254.128:3689/databases/36/containers/1234/items?session-id=2025037772&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,dmap.containeritemid,com.apple.tunes.has-video
		PlaylistSongs playlistSongs = RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%s/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartst,daap.songalbum,daap.songtime,dmap.containeritemid,com.apple.tunes.has-video", session.getRequestBase(), session.getDatabase().getItemId(), playlistid, session.getSessionId()), false);
		return Iterables.size(playlistSongs.getListing().getListingItems());
	}

	public int readRadioPlaylist(String playlistid) throws Exception
	{
		// GET /databases/24691/containers/24699/items?
		// meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,
		// dmap.containeritemid,com.apple.itunes.has-video,daap.songdisabled,
		// com.apple.itunes.mediakind,daap.songdescription
		// &type=music&session-id=345827905
		PlaylistSongs playlistSongs = RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%s/items?" + "meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum," + "dmap.containeritemid,com.apple.itunes.has-video,daap.songdisabled," + "com.apple.itunes.mediakind,daap.songdescription" + "&type=music&session-id=%s", session.getRequestBase(), session.getRadioDatabase().getItemId(), playlistid, session.getSessionId()), false);
		return Iterables.size(playlistSongs.getListing().getListingItems());
	}

	public void readNowPlaying(String albumid) throws Exception
	{
		// Try Wilco (Alex W)'s nowplaying extension /ctrl-int/1/items
		try
		{
			Object o = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:%s'", session.getRequestBase(), session.getSessionId(), albumid), false);
			System.out.println(o);
		}
		catch(IOException e)
		{
			readCurrentSong();
		}
	}

	public void readCurrentSong() throws Exception
	{
		// reads the current playing song as a one-item playlist
		// Refactor response into one that looks like a normal items request
		// and trigger listener
		UnknownST state = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=1&session-id=%s", session.getRequestBase(), session.getSessionId(), false));

		int revision = state.getSpecificChunk(StatusRevision.class).getValue();

		state.getSpecificChunk(PlayStatus.class);
		state.getSpecificChunk(ShuffleStatus.class);
		state.getSpecificChunk(RepeatStatus.class);
		state.getSpecificChunk(VisualizerStatus.class);
		state.getSpecificChunk(FullscreenStatus.class);
		state.getSpecificChunk(GeniusSelectable.class);

		long trackId = state.getSpecificChunk(NowPlaying.class).getTrackId();
		state.getSpecificChunk(TrackName.class);
		state.getSpecificChunk(TrackArtist.class);
		state.getSpecificChunk(TrackAlbum.class);
		state.getSpecificChunk(TrackGenre.class);
		state.getSpecificChunk(SongAlbumId.class);

		state.getSpecificChunk(ProgressRemain.class);
		state.getSpecificChunk(ProgressTotal.class);
	}

	/**
	 * URL encode a string escaping single quotes first.
	 * <p>
	 * 
	 * @param input
	 *            the string to encode
	 * @return the URL encoded string value
	 */
	public static String escapeUrlString(final String input)
	{
		String encoded = "";
		try
		{
			encoded = URLEncoder.encode(input, "UTF-8");
			encoded = encoded.replaceAll("\\+", "%20");
			encoded = encoded.replaceAll("%27", "%5C'");
		}
		catch(UnsupportedEncodingException e)
		{
			logger.warn("escapeUrlString Exception:" + e.getMessage());
		}
		return encoded;
	}
}
