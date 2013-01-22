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

import org.dyndns.jkiddo.protocol.dmap.chunks.daap.DatabaseBrowse;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.DatabaseSongs;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.PlaylistSongs;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.SongUserRating;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ListingItem;
import org.dyndns.jkiddo.protocol.dmap.chunks.unknown.UnknownAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Library
{

	public final static Logger logger = LoggerFactory.getLogger(Library.class);

	final Session session;

	Library(Session session)
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
	public PlaylistSongs readSearch(String search, long start, long items) throws Exception
	{
		final String encodedSearch = RequestHelper.escapeUrlString(search);
		return RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=name&include-sort-headers=1&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+('dmap.itemname:*%s*','daap.songartist:*%s*','daap.songalbum:*%s*'))&index=%d-%d", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), encodedSearch, encodedSearch, encodedSearch, start, items), false);
	}

	public DatabaseBrowse getAllArtists() throws Exception
	{
		// request ALL artists for performance
		// /databases/%d/browse/artists?session-id=%s&include-sort-headers=1&index=%d-%d
		return RequestHelper.requestParsed(String.format("%s/databases/%d/browse/artists?session-id=%s&include-sort-headers=1", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId()), false, true);
	}

	public UnknownAL getAlbums(String artist) throws Exception
	{
		final String encodedArtist = RequestHelper.escapeUrlString(artist);
		// make albums request for this artist
		// http://192.168.254.128:3689/databases/36/groups?session-id=1034286700&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1

		return RequestHelper.requestParsed(String.format("%s/databases/%d/groups?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1&query='daap.songartist:%s'", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId(), encodedArtist), false);
	}

	public UnknownAL getAllAlbums() throws Exception
	{
		// make partial album list request
		// http://192.168.254.128:3689/databases/36/groups?session-id=1034286700&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1&index=0-50
		return RequestHelper.requestParsed(String.format("%s/databases/%d/groups?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=album&include-sort-headers=1", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId()), false);
	}

	public PlaylistSongs getAllTracks() throws Exception
	{
		// make tracks list request
		// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
		return RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId()), false);
	}

	public PlaylistSongs getTracks(String albumid) throws Exception
	{
		// make tracks list request
		// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
		return RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:%s'", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), albumid), false);
	}

	public PlaylistSongs getAllTracks(String artist) throws Exception
	{
		final String encodedArtist = RequestHelper.escapeUrlString(artist);

		// make tracks list request
		// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
		return RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songartist:%s'", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), encodedArtist), false);
	}

	public PlaylistSongs getPlaylistSongs(String playlistid) throws Exception
	{
		// http://192.168.254.128:3689/databases/36/containers/1234/items?session-id=2025037772&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,dmap.containeritemid,com.apple.tunes.has-video
		return RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%s/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartst,daap.songalbum,daap.songtime,dmap.containeritemid,com.apple.tunes.has-video", session.getRequestBase(), session.getDatabase().getItemId(), playlistid, session.getSessionId()), false);
	}

	public PlaylistSongs getRadioPlaylist(String playlistid) throws Exception
	{
		// GET /databases/24691/containers/24699/items?
		// meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,
		// dmap.containeritemid,com.apple.itunes.has-video,daap.songdisabled,
		// com.apple.itunes.mediakind,daap.songdescription
		// &type=music&session-id=345827905
		return RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%s/items?" + "meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum," + "dmap.containeritemid,com.apple.itunes.has-video,daap.songdisabled," + "com.apple.itunes.mediakind,daap.songdescription" + "&type=music&session-id=%s", session.getRequestBase(), session.getRadioDatabase().getItemId(), playlistid, session.getSessionId()), false);
	}

	public SongUserRating getTrackRating(long trackId) throws Exception
	{
		// MonkeyTunes style would be with PlaylistSongs instead of DatabaseSongs
		final DatabaseSongs databaseSongs = RequestHelper.requestParsed(String.format("%s/databases/%d/items?session-id=%s&meta=daap.songuserrating&type=music&query='dmap.itemid:%d'", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId(), trackId));
		final ListingItem listingItem = databaseSongs.getListing().getSingleListingItemContainingClass(SongUserRating.class);
		return listingItem.getSpecificChunk(SongUserRating.class);
	}
}
