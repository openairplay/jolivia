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

import org.dyndns.jkiddo.protocol.dmap.Database;
import org.dyndns.jkiddo.protocol.dmap.Playlist;
import org.dyndns.jkiddo.protocol.dmap.chunks.Chunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.BasePlaylist;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.DatabasePlaylists;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.ServerDatabases;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ContentCodesResponse;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.DatabaseShareType;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemCount;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemId;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemName;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ListingItem;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.LoginResponse;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.PersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

public class Session
{
	public final static Logger logger = LoggerFactory.getLogger(Session.class);

	private final String host;
	private final int sessionId;
	private final Database database, radioDatabase;

	public final int getSessionId()
	{
		return sessionId;
	}

	public final Database getDatabase()
	{
		return database;
	}

	public final Database getRadioDatabase()
	{
		return radioDatabase;
	}

	public Session(String host, String pairingGuid) throws Exception
	{
		// start a session with the itunes server
		this.host = host;

		// http://192.168.254.128:3689/login?pairing-guid=0x0000000000000001
		logger.debug(String.format("trying login for host=%s and guid=%s", host, pairingGuid));
		LoginResponse loginResponse = RequestHelper.requestParsed(String.format("%s/login?pairing-guid=0x%s", this.getRequestBase(), pairingGuid));

		// Response login = RequestHelper.requestParsed(String.format("%s/login?pairing-guid=0x%s", this.getRequestBase(), pairingGuid), false);
		// this.sessionId = login.getNested("mlog").getNumberString("mlid");
		this.sessionId = loginResponse.getSessionId().getValue();
		logger.debug(String.format("found session-id=%s", this.sessionId));

		// http://192.168.254.128:3689/databases?session-id=1301749047
		ServerDatabases serverDatabases = RequestHelper.requestParsed(String.format("%s/databases?session-id=%s", this.getRequestBase(), this.sessionId));

		// Local database
		{
			// For now, the LocalDatabase is sufficient
			ListingItem localDatabase = serverDatabases.getListing().getSingleListingItem(new Predicate<ListingItem>() {
				@Override
				public boolean apply(ListingItem input)
				{
					return DatabaseShareType.LOCAL == input.getSpecificChunk(DatabaseShareType.class).getValue();
				}
			});

			String databaseName = localDatabase.getSpecificChunk(ItemName.class).getValue();
			int itemId = localDatabase.getSpecificChunk(ItemId.class).getValue();
			long persistentId = localDatabase.getSpecificChunk(PersistentId.class).getUnsignedValue().longValue();

			// fetch playlists to find the overall magic "Music" playlist
			DatabasePlaylists allPlaylists = RequestHelper.requestParsed(String.format("%s/databases/%d/containers?session-id=%s&meta=dmap.itemname,dmap.itemcount,dmap.itemid,dmap.persistentid,daap.baseplaylist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,com.apple.itunes.saved-genius,dmap.parentcontainerid,dmap.editcommandssupported", this.getRequestBase(), itemId, this.sessionId));

			// For now, the BasePlayList is sufficient
			ListingItem item = allPlaylists.getListing().getSingleListingItemContainingClass(BasePlaylist.class);

			Playlist playlist = new Playlist(item.getSpecificChunk(ItemName.class).getValue(), item.getSpecificChunk(PersistentId.class).getUnsignedValue().longValue(), item.getSpecificChunk(ItemId.class).getUnsignedValue(), item.getSpecificChunk(ItemCount.class).getUnsignedValue());
			database = new Database(databaseName, itemId, persistentId, playlist);
		}

		// Radio database
		{
			ListingItem database = serverDatabases.getListing().getSingleListingItem(new Predicate<ListingItem>() {
				@Override
				public boolean apply(ListingItem input)
				{
					return DatabaseShareType.RADIO == input.getSpecificChunk(DatabaseShareType.class).getValue();
				}
			});

			String databaseName = database.getSpecificChunk(ItemName.class).getValue();
			int itemId = database.getSpecificChunk(ItemId.class).getValue();
			long persistentId = database.getSpecificChunk(PersistentId.class).getUnsignedValue().longValue();
			radioDatabase = new Database(databaseName, itemId, persistentId);

			DatabasePlaylists allPlaylists = RequestHelper.requestParsed(String.format("%s/databases/%d/containers?session-id=%s&meta=dmap.itemname,dmap.itemcount,dmap.itemid,dmap.persistentid,daap.baseplaylist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,com.apple.itunes.saved-genius,dmap.parentcontainerid,dmap.editcommandssupported", this.getRequestBase(), itemId, this.sessionId));

			Iterable<ListingItem> items = allPlaylists.getListing().getListingItems();
			for(ListingItem item : items)
			{
				Playlist playlist = new Playlist(item.getSpecificChunk(ItemName.class).getValue(), 0, item.getSpecificChunk(ItemId.class).getUnsignedValue(), item.getSpecificChunk(ItemCount.class).getUnsignedValue());
				logger.debug(String.format("found radio genre=%s", playlist.getName()));
				radioDatabase.addPlaylist(null, playlist);

			}
		}
	}

	public String getRequestBase()
	{
		return String.format("http://%s:3689", host);
	}

	/**
	 * Logout method disconnects the session on the server. This is being a good DACP citizen that was not happening in previous versions.
	 * 
	 * @throws Exception
	 */
	public void logout() throws Exception
	{
		RequestHelper.request(String.format("%s/logout?session-id=%s", this.getRequestBase(), this.sessionId));
	}

	public void controlPause() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/pause?session-id=130883770
		RequestHelper.request(String.format("%s/ctrl-int/1/pause?session-id=%s", this.getRequestBase(), this.sessionId));
	}
	public void controlPlay() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/playpause?session-id=130883770
		RequestHelper.request(String.format("%s/ctrl-int/1/playpause?session-id=%s", this.getRequestBase(), this.sessionId));
	}

	public void controlNext() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/nextitem?session-id=130883770
		RequestHelper.request(String.format("%s/ctrl-int/1/nextitem?session-id=%s", this.getRequestBase(), this.sessionId));
	}

	public void controlPrev() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/previtem?session-id=130883770
		RequestHelper.request(String.format("%s/ctrl-int/1/previtem?session-id=%s", this.getRequestBase(), this.sessionId));
	}

	public void controlVolume(long volume) throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/setproperty?dmcp.volume=100.000000&session-id=130883770
		RequestHelper.request(String.format("%s/ctrl-int/1/setproperty?dmcp.volume=%s&session-id=%s", this.getRequestBase(), volume, this.sessionId));
	}

	public void controlProgress(int progressSeconds) throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/setproperty?dacp.playingtime=82784&session-id=130883770
		RequestHelper.request(String.format("%s/ctrl-int/1/setproperty?dacp.playingtime=%d&session-id=%s", this.getRequestBase(), progressSeconds * 1000, this.sessionId));
	}

	public void controlShuffle(int shuffleMode) throws Exception
	{
		// /ctrl-int/1/setproperty?dacp.shufflestate=1&session-id=1873217009
		RequestHelper.request(String.format("%s/ctrl-int/1/setproperty?dacp.shufflestate=%d&session-id=%s", this.getRequestBase(), shuffleMode, this.sessionId));
	}

	public void controlRepeat(int repeatMode) throws Exception
	{
		// /ctrl-int/1/setproperty?dacp.repeatstate=2&session-id=1873217009
		// HTTP/1.1
		RequestHelper.request(String.format("%s/ctrl-int/1/setproperty?dacp.repeatstate=%d&session-id=%s", this.getRequestBase(), repeatMode, this.sessionId));
	}

	/**
	 * Sets the rating stars of a particular song 0-100.
	 * <p/>
	 * 
	 * @param rating
	 *            the rating 0-100 to set for rating stars
	 * @param trackId
	 *            the id of the track to update the rating for
	 * @throws Exception
	 */
	public void controlRating(final long rating, final long trackId) throws Exception
	{
		RequestHelper.request(String.format("%s/ctrl-int/1/setproperty?dacp.userrating=%d&song-spec='dmap.itemid:%d'&session-id=%s", this.getRequestBase(), rating, trackId, this.sessionId));
	}

	/**
	 * Command to clear the Now Playing cue.
	 * 
	 * @throws Exception
	 */
	private void controlClearCue() throws Exception
	{
		RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=clear&session-id=%s", getRequestBase(), sessionId));
	}

	public void controlPlayAlbum(final long albumId, final int tracknum) throws Exception
	{

		// http://192.168.254.128:3689/ctrl-int/1/cue?command=clear&session-id=130883770
		// http://192.168.254.128:3689/ctrl-int/1/cue?command=play&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:32')+'daap.songartist:Family%20Force%205')&index=0&sort=album&session-id=130883770
		// /ctrl-int/1/cue?command=play&query='daap.songalbumid:16621530181618739404'&index=11&sort=album&session-id=514488449

		// GET
		// /ctrl-int/1/playspec?database-spec='dmap.persistentid:16621530181618731553'&playlist-spec='dmap.persistentid:9378496334192532210'&dacp.shufflestate=1&session-id=514488449
		// (zero based index into playlist)

		controlClearCue();
		RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=play&query='daap.songalbumid:%s'&index=%d&sort=album&session-id=%s", getRequestBase(), albumId, tracknum, sessionId));

	}

	public void controlQueueAlbum(final long albumId) throws Exception
	{
		RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=add&query='daap.songalbumid:%s'&session-id=%s", getRequestBase(), albumId, sessionId));
	}

	public void controlPlayArtist(String artist, int index) throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/cue?command=clear&session-id=130883770
		// /ctrl-int/1/cue?command=play&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:32')+'daap.songartist:Family%20Force%205')&index=0&sort=album&session-id=130883770
		// /ctrl-int/1/cue?command=play&query='daap.songartist:%s'&index=0&sort=album&session-id=%s

		final String encodedArtist = Library.escapeUrlString(artist);
		final int encodedIndex = index;

		controlClearCue();
		RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=play&query='daap.songartist:%s'&index=%d&sort=album&session-id=%s", getRequestBase(), encodedArtist, encodedIndex, sessionId));
	}

	public void controlQueueArtist(String artist) throws Exception
	{
		final String encodedArtist = Library.escapeUrlString(artist);
		RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=add&query='daap.songartist:%s'&session-id=%s", getRequestBase(), encodedArtist, sessionId));
	}

	public void controlQueueTrack(final long trackId) throws Exception
	{
		RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=add&query='dmap.itemid:%s'&session-id=%s", getRequestBase(), trackId, sessionId));
	}

	public void controlPlayTrack(final long trackId) throws Exception
	{
		controlClearCue();
		RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=play&query='dmap.itemid:%s'&session-id=%s", getRequestBase(), trackId, sessionId));
	}

	public void controlPlaySearch(final String search, final int index) throws Exception
	{
		// /ctrl-int/1/cue?command=play&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+'dmap.itemname:*F*')&index=4&sort=name&session-id=1550976127
		final String encodedSearch = Library.escapeUrlString(search);
		controlClearCue();
		RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=play&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+('dmap.itemname:*%s*','daap.songartist:*%s*','daap.songalbum:*%s*'))&type=music&sort=name&index=%d&session-id=%s", getRequestBase(), encodedSearch, encodedSearch, encodedSearch, index, sessionId));
	}

	public void controlPlayPlaylist(final String playlistPersistentId, final String containerItemId) throws Exception
	{
		// /ctrl-int/1/playspec?database-spec='dmap.persistentid:0x9031099074C14E05'&container-spec='dmap.persistentid:0xA1E1854E0B9A1B'&container-item-spec='dmap.containeritemid:0x1b47'&session-id=7491138
		RequestHelper.request(String.format("%s/ctrl-int/1/playspec?database-spec='dmap.persistentid:0x%s'&container-spec='dmap.persistentid:0x%s'&container-item-spec='dmap.containeritemid:0x%s'&session-id=%s", getRequestBase(), database.getPersistentId(), playlistPersistentId, containerItemId, sessionId));
	}

	public void controlPlayIndex(final String albumid, final int tracknum) throws Exception
	{
		try
		{
			RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=play&index=%d&sort=album&session-id=%s", getRequestBase(), tracknum, sessionId));
			// on iTunes this generates a 501 Not Implemented response
		}
		catch(Exception e)
		{
			if(albumid != null && albumid.length() > 0)
			{
				// Fall back to choosing from the current album if there is
				// one
				controlClearCue();
				RequestHelper.request(String.format("%s/ctrl-int/1/cue?command=play&query='daap.songalbumid:%s'&index=%d&sort=album&session-id=%s", getRequestBase(), albumid, tracknum, sessionId));
			}
		}
	}

	public void controlVisualiser(boolean enabled) throws Exception
	{
		// GET /ctrl-int/1/setproperty?dacp.visualizer=1&session-id=283658916
		RequestHelper.request(String.format("%s/ctrl-int/1/setproperty?dacp.visualizer=%d&session-id=%s", this.getRequestBase(), enabled ? 1 : 0, this.sessionId));
	}

	public void controlFullscreen(boolean enabled) throws Exception
	{
		// GET /ctrl-int/1/setproperty?dacp.fullscreen=1&session-id=283658916
		RequestHelper.request(String.format("%s/ctrl-int/1/setproperty?dacp.fullscreen=%d&session-id=%s", this.getRequestBase(), enabled ? 1 : 0, this.sessionId));
	}

	public void controlPlayRadio(final long genreId, final long itemId) throws Exception
	{
		playSpec(radioDatabase.getItemId(), genreId, itemId);
	}

	public void playSpec(final long databaseId, final long containerId, final long itemId) throws Exception
	{
		// GET
		// /ctrl-int/1/playspec?database-spec='dmap.itemid:0x6073'&container-spec='dmap.itemid:0x607B'&item-spec='dmap.itemid:0x7cbe'&session-id=345827905
		RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playspec?" + "database-spec='dmap.itemid:0x%x'" + "&container-spec='dmap.itemid:0x%x'" + "&item-spec='dmap.itemid:0x%x'" + "&session-id=%s", getRequestBase(), databaseId, containerId, itemId, sessionId));
	}

	// Query the media server about the content codes it handles
	// print to stderr as a csv file
	public void listContentCodes() throws Exception
	{
		ContentCodesResponse contentcodes = RequestHelper.requestParsed(String.format("%s/content-codes?session-id=%s", this.getRequestBase(), this.sessionId));
		for(Chunk contentCode : contentcodes)
		{
			logger.info(contentCode.getContentCodeString());
		}
	}
}
