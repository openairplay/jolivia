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

package org.tunesremote.daap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tunesremote.PlaylistListener;
import org.tunesremote.TagListener;

public class Library
{

	public final static String TAG = Library.class.toString();
	public final static int RESULT_INCREMENT = 50;
	public final static Pattern MLIT_PATTERN = Pattern.compile("mlit");
	public final static Logger logger = LoggerFactory.getLogger(Library.class);
	// library keeps track of albums/tracks from itunes also caches requests as
	// needed
	protected final Session session;

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
	 */
	public long readSearch(TagListener listener, String search, long start, long items)
	{
		long total = -1;
		try
		{
			String encodedSearch = Library.escapeUrlString(search);
			String query = String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=name&include-sort-headers=1&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+('dmap.itemname:*%s*','daap.songartist:*%s*','daap.songalbum:*%s*'))&index=%d-%d", session.getRequestBase(), session.databaseId, session.libraryId, session.sessionId, encodedSearch, encodedSearch, encodedSearch, start, items);
			byte[] raw = RequestHelper.request(query, false);
			Response resp = ResponseParser.performParse(raw, listener, MLIT_PATTERN);
			// apso or adbs
			Response nested = resp.getNested("apso");
			if(nested == null)
				nested = resp.getNested("adbs");
			if(nested != null)
				total = nested.getNumberLong("mtco");
		}
		catch(Exception e)
		{
			logger.warn(TAG, "readSearch Exception:" + e.getMessage());
		}

		logger.debug(TAG, String.format("readSearch() finished start=%d, items=%d, total=%d", start, items, total));

		return total;
	}

	public void readArtists(TagListener listener)
	{
		// check if we have a local cache create a wrapping taglistener to create
		// local cache
		try
		{
			logger.debug(TAG, "readArtists() requesting...");

			// request ALL artists for performance
			// GET
			// /databases/%d/browse/artists?session-id=%s&include-sort-headers=1&index=%d-%d
			byte[] raw = RequestHelper.request(String.format("%s/databases/%d/browse/artists?session-id=%s&include-sort-headers=1", session.getRequestBase(), session.databaseId, session.sessionId), false);

			// parse list, passing off events in the process
			int hits = ResponseParser.performSearch(raw, listener, MLIT_PATTERN, true);
			logger.debug(TAG, String.format("readArtists() total=%d", hits));
			raw = null;

		}
		catch(Exception e)
		{
			logger.warn(TAG, "readArtists Exception:" + e.getMessage());
		}
	}

	public void readAlbums(TagListener listener, String artist)
	{

		final String encodedArtist = Library.escapeUrlString(artist);

		try
		{

			// make albums request for this artist
			// http://192.168.254.128:3689/databases/36/groups?session-id=1034286700&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1

			byte[] raw = RequestHelper.request(String.format("%s/databases/%d/groups?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1&query='daap.songartist:%s'", session.getRequestBase(), session.databaseId, session.sessionId, encodedArtist), false);

			// parse list, passing off events in the process
			ResponseParser.performSearch(raw, listener, MLIT_PATTERN, false);

		}
		catch(Exception e)
		{
			logger.warn(TAG, "readAlbums Exception:" + e.getMessage());
		}

	}

	public void readAlbums(TagListener listener)
	{
		try
		{
			byte[] raw = null;

			// make partial album list request
			// http://192.168.254.128:3689/databases/36/groups?session-id=1034286700&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1&index=0-50
			raw = RequestHelper.request(String.format("%s/databases/%d/groups?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=album&include-sort-headers=1", session.getRequestBase(), session.databaseId, session.sessionId), false);

			// parse list, passing off events in the process
			final int hits = ResponseParser.performSearch(raw, listener, MLIT_PATTERN, false);
			logger.info(TAG, "readAlbums Total:" + hits);
		}
		catch(Exception e)
		{
			logger.warn(TAG, "readAlbums Exception:" + e.getMessage());
		}
	}

	public void readTracks(String albumid, TagListener listener)
	{
		try
		{
			String temp = String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:%s'", session.getRequestBase(), session.databaseId, session.libraryId, session.sessionId, albumid);

			// make tracks list request
			// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
			byte[] raw = RequestHelper.request(temp, false);

			// parse list, passing off events in the process
			ResponseParser.performSearch(raw, listener, MLIT_PATTERN, false);

		}
		catch(Exception e)
		{
			logger.warn(TAG, "readTracks Exception:" + e.getMessage());
		}

	}

	public void readAllTracks(String artist, TagListener listener)
	{

		// check if we have a local cache create a wrapping taglistener to create
		// local cache
		final String encodedArtist = Library.escapeUrlString(artist);

		try
		{
			// make tracks list request
			// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
			byte[] raw = RequestHelper.request(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songartist:%s'", session.getRequestBase(), session.databaseId, session.libraryId, session.sessionId, encodedArtist), false);

			// parse list, passing off events in the process
			ResponseParser.performSearch(raw, listener, MLIT_PATTERN, false);

		}
		catch(Exception e)
		{
			logger.warn(TAG, "readTracks Exception:" + e.getMessage());
		}
	}

	public void readPlaylists(PlaylistListener listener)
	{
		for(Playlist ply : this.session.playlists)
		{
			listener.foundPlaylist(ply);
		}
		listener.searchDone();
	}

	public void readPlaylist(String playlistid, TagListener listener)
	{
		logger.debug(TAG, " in readPlaylists");
		try
		{
			// http://192.168.254.128:3689/databases/36/containers/1234/items?session-id=2025037772&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,dmap.containeritemid,com.apple.tunes.has-video
			byte[] raw = RequestHelper.request(String.format("%s/databases/%d/containers/%s/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartst,daap.songalbum,daap.songtime,dmap.containeritemid,com.apple.tunes.has-video", session.getRequestBase(), session.databaseId, playlistid, session.sessionId), false);

			// parse list, passing off events in the process
			ResponseParser.performSearch(raw, listener, MLIT_PATTERN, false);

		}
		catch(Exception e)
		{
			logger.warn(TAG, "readPlaylists Exception:" + e.getMessage());
		}
	}

	public void readRadioPlaylists(PlaylistListener listener)
	{
		if(this.session.supportsRadio())
		{
			for(Playlist ply : this.session.getRadioGenres())
			{
				listener.foundPlaylist(ply);
			}
		}
		listener.searchDone();
	}

	public void readRadioPlaylist(String playlistid, TagListener listener)
	{
		logger.debug(TAG, " in readRadioPlaylist");
		try
		{
			// GET /databases/24691/containers/24699/items?
			// meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,
			// dmap.containeritemid,com.apple.itunes.has-video,daap.songdisabled,
			// com.apple.itunes.mediakind,daap.songdescription
			// &type=music&session-id=345827905
			byte[] raw = RequestHelper.request(String.format("%s/databases/%d/containers/%s/items?" + "meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum," + "dmap.containeritemid,com.apple.itunes.has-video,daap.songdisabled," + "com.apple.itunes.mediakind,daap.songdescription" + "&type=music&session-id=%s", session.getRequestBase(), session.radioDatabaseId, playlistid, session.sessionId), false);

			// parse list, passing off events in the process
			ResponseParser.performSearch(raw, listener, MLIT_PATTERN, false);

		}
		catch(Exception e)
		{
			logger.warn(TAG, "readRadioPlaylist Exception:" + e.getMessage());
		}
	}

	public boolean readNowPlaying(String albumid, TagListener listener)
	{

		// Try Wilco (Alex W)'s nowplaying extension /ctrl-int/1/items
		try
		{
			String query = String.format("%s/ctrl-int/1/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:%s'", session.getRequestBase(), session.sessionId, albumid);

			byte[] raw = RequestHelper.request(query, false);

			// parse list, passing off events in the process
			ResponseParser.performSearch(raw, listener, MLIT_PATTERN, false);
			return false;

		}
		catch(Exception e)
		{
			// Fall back to reading album
			if(albumid != null && albumid.length() > 0)
				readTracks(albumid, listener);
			else
				readCurrentSong(listener);

			return true;
		}

	}

	public void readCurrentSong(TagListener listener)
	{
		// reads the current playing song as a one-item playlist
		try
		{
			String temp = String.format("%s/ctrl-int/1/playstatusupdate?revision-number=1&session-id=%s", session.getRequestBase(), session.sessionId);

			// Refactor response into one that looks like a normal items request
			// and trigger listener
			Response resp = RequestHelper.requestParsed(temp, false).getNested("cmst");
			if(resp.containsKey("cann"))
			{
				Response new_item = new Response();
				new_item.put("minm", resp.getString("cann"));
				new_item.put("asal", resp.getString("canl"));
				new_item.put("asar", resp.getString("cana"));
				new_item.put("astm", resp.getString("cast"));

				listener.foundTag("mlit", new_item);
			}
			listener.searchDone();
		}
		catch(Exception e)
		{
			logger.warn(TAG, "readCurrentSong Exception:" + e.getMessage());
		}
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
			logger.warn(TAG, "escapeUrlString Exception:" + e.getMessage());
		}
		return encoded;
	}
}
