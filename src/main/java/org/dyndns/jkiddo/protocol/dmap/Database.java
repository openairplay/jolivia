/*
 * Digital Audio Access Protocol (DAAP) Library
 * Copyright (C) 2004-2010 Roger Kapsi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dyndns.jkiddo.protocol.dmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Database is a container for Playlists and it keeps track of all Songs in the Database whereat it is not responsible for the actual management of the Songs (it's only interested in the Song IDs).
 * 
 * @author Roger Kapsi
 */
public class Database
{

	private static final Logger LOG = LoggerFactory.getLogger(Database.class);

	/** databaseId is an 32bit unsigned value! */
	private static final AtomicLong DATABASE_ID = new AtomicLong();

	/** unique id */
	private final long itemId;

	/** unique persistent id */
	private final long persistentId;

	/** Name of this Database */
	private String name;

	/**
	 * The total number of Playlists in this Database
	 */
	private int totalPlaylistCount = 0;

	/**
	 * The total number of Songs in this Database
	 */
	private int totalSongCount = 0;

	/** A List of Playlists */
	private final List<Playlist> playlists = new ArrayList<Playlist>();

	/** Set of deleted playlists */
	private Set<Playlist> deletedPlaylists = null;

	/** Set of deleted Songs */
	private Set<Song> deletedSongs = null;

	/** master playlist */
	private Playlist masterPlaylist = null;

	protected Database(Database database, Transaction txn)
	{
		this.itemId = database.itemId;
		this.persistentId = database.persistentId;
		this.name = database.name;

		if(database.deletedPlaylists != null)
		{
			this.deletedPlaylists = database.deletedPlaylists;
			database.deletedPlaylists = null;
		}

		Set<Song> songs = database.getSongs();

		for(Playlist playlist : database.playlists)
		{
			if(txn.modified(playlist))
			{
				if(deletedPlaylists == null || !deletedPlaylists.contains(playlist))
				{
					Playlist clone = new Playlist(playlist, txn);
					playlists.add(clone);

					if(playlist == database.masterPlaylist)
					{
						this.masterPlaylist = clone;
					}
				}

				Set<Song> deletedSongs = playlist.getDeletedSongs();
				if(deletedSongs != null && !deletedSongs.isEmpty())
				{
					if(this.deletedSongs == null)
					{
						this.deletedSongs = new HashSet<Song>(deletedSongs);
					}
					else
					{
						this.deletedSongs.addAll(deletedSongs);
					}
				}
			}
		}

		if(deletedSongs != null)
		{
			deletedSongs.removeAll(songs);
		}

		this.totalPlaylistCount = database.playlists.size();
		this.totalSongCount = songs.size();
	}

	public Database(String name)
	{
		this(name, new Playlist(name));
	}

	/**
	 * Create a new Database with the name
	 * 
	 * @param name
	 *            a name for this Database
	 */
	public Database(String name, Playlist masterPlaylist)
	{
		this(name, DATABASE_ID.getAndIncrement(), Library.nextPersistentId(), masterPlaylist);
	}
	
	public Database(String name, long itemId, long persistentId)
	{
		this(name, itemId, persistentId, new Playlist(name));
	}

	public Database(String name, long itemId, long persistentId, Playlist playlist)
	{
		this.itemId = itemId;
		this.persistentId = persistentId;
		this.name = name;
		this.totalPlaylistCount = 0;
		this.totalSongCount = 0;

		this.masterPlaylist = playlist;
		addPlaylistP(null, masterPlaylist);
	}

	/**
	 * Returns the unique id of this Database
	 * 
	 * @return unique id of this Database
	 */
	public long getItemId()
	{
		return itemId;
	}

	/**
	 * Returns the name of this Database.
	 * 
	 * @return name of this Database
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of this Database.
	 * 
	 * @param new name
	 */
	public void setName(Transaction txn, final String name)
	{
		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					setNameP(txn, name);
				}
			});
		}
		else
		{
			setNameP(txn, name);
		}

		masterPlaylist.setName(txn, name);
	}

	private void setNameP(Transaction txn, String name)
	{
		this.name = name;
	}

	/**
	 * The persistent id of this Database. Unused at the moment!
	 * 
	 * @return the persistent id of this Database
	 */
	public long getPersistentId()
	{
		return persistentId;
	}

	/**
	 * Returns the master Playlist. The master Playlist is created automatically by the Database! There's no technical difference between a master Playlist and a usual Playlist except that it cannot be removed from the Database.
	 * 
	 * @return the master Playlist
	 */
	public Playlist getMasterPlaylist()
	{
		return masterPlaylist;
	}

	/**
	 * Returns an unmodifiable Set with all Playlists in this Database
	 * 
	 * @return unmodifiable Set of Playlists
	 */
	public List<Playlist> getPlaylists()
	{
		return Collections.unmodifiableList(playlists);
	}

	public void addPlaylists(Transaction txn, final Collection<Playlist> playlists)
	{
		for(Playlist p : playlists)
		{
			addPlaylist(txn, p);
		}
	}
	/**
	 * Adds playlist to this Database
	 * 
	 * @param txn
	 *            a Transaction
	 * @param playlist
	 *            the Playliost to add
	 */
	public void addPlaylist(Transaction txn, final Playlist playlist)
	{
		if(masterPlaylist.equals(playlist))
		{
			throw new DmapException("You cannot add the master playlist.");
		}

		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					addPlaylistP(txn, playlist);
				}
			});
			txn.attach(playlist);
		}
		else
		{
			addPlaylistP(txn, playlist);
		}
	}

	private void addPlaylistP(Transaction txn, Playlist playlist)
	{
		if(!containsPlaylist(playlist) && playlists.add(playlist))
		{
			totalPlaylistCount = playlists.size();
			if(deletedPlaylists != null && deletedPlaylists.remove(playlist) && deletedPlaylists.isEmpty())
			{
				deletedPlaylists = null;
			}
		}
	}

	/**
	 * Removes playlist from this Database
	 * 
	 * @param txn
	 *            a Transaction
	 * @param playlist
	 *            the Playlist to remove
	 */
	public void removePlaylist(Transaction txn, final Playlist playlist)
	{
		if(masterPlaylist.equals(playlist))
		{
			throw new DmapException("You cannot remove the master playlist.");
		}

		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					removePlaylistP(txn, playlist);
				}
			});
		}
		else
		{
			removePlaylistP(txn, playlist);
		}
	}

	private void removePlaylistP(Transaction txn, Playlist playlist)
	{
		if(playlists.remove(playlist))
		{
			totalPlaylistCount = playlists.size();

			if(deletedPlaylists == null)
			{
				deletedPlaylists = new HashSet<Playlist>();
			}
			deletedPlaylists.add(playlist);
		}
	}

	/**
	 * Returns the number of Playlists in this Database
	 */
	public int getPlaylistCount()
	{
		return playlists.size();
	}

	/**
	 * Returns true if playlist is in this Database
	 * 
	 * @param playlist
	 * @return true if Database contains playlist
	 */
	public boolean containsPlaylist(Playlist playlist)
	{
		return playlists.contains(playlist);
	}

	@Override
	public String toString()
	{
		return "Database(" + getItemId() + ", " + getName() + ")";
	}

	@Override
	public int hashCode()
	{
		return (int) (getItemId() & Integer.MAX_VALUE);
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Database))
		{
			return false;
		}
		return ((Database) o).getItemId() == getItemId();
	}

	/**
	 * Returns all Songs in this Database
	 */
	public Set<Song> getSongs()
	{
		Set<Song> songs = null;
		for(Playlist playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				if(songs == null)
				{
					songs = new HashSet<Song>(playlist.getSongs());
				}
				else
				{
					songs.addAll(playlist.getSongs());
				}
			}
		}
		if(songs == null)
		{
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(songs);
	}

	/**
	 * Returns the number of Songs in this Database
	 */
	public int getSongCount()
	{
		return getSongs().size();
	}

	/**
	 * Returns true if song is in this Database
	 */
	public boolean containsSong(Song song)
	{
		for(Playlist playlist : playlists)
		{
			if(playlist.containsSong(song))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds Song to all Playlists
	 * 
	 * @param txn
	 * @param song
	 */
	public void addSong(Transaction txn, Song song)
	{
		for(Playlist playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				playlist.addSong(txn, song);
			}
		}
	}

	public void setSongs(Transaction txn, Collection<Song> songs)
	{
		for(Playlist playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				playlist.setSongs(txn, songs);
			}
		}
	}

	/**
	 * Removes Song from all Playlists
	 * 
	 * @param txn
	 * @param song
	 */
	public void removeSong(Transaction txn, Song song)
	{
		for(Playlist playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				playlist.removeSong(txn, song);
			}
		}
	}

	public Set<Playlist> getSongPlaylists(Song song)
	{
		Set<Playlist> ret = null;
		for(Playlist playlist : playlists)
		{
			if(playlist.containsSong(song))
			{
				if(ret == null)
				{
					ret = new HashSet<Playlist>();
				}
				ret.add(playlist);
			}
		}

		if(ret != null)
		{
			return Collections.unmodifiableSet(ret);
		}

		return Collections.emptySet();
	}

	/**
	 * Gets and returns a Song by its ID
	 * 
	 * @param songId
	 * @return
	 */
	public Song getSong(String itemId)
	{
		for(Playlist playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				Song song = playlist.getSong(itemId);
				if(song != null)
				{
					return song;
				}
			}
		}
		return null;
	}

	/**
	 * Gets and returns a Playlist by its ID
	 * 
	 * @param playlistId
	 * @return
	 */
	public Playlist getPlaylist(long playlistId)
	{
		for(Playlist playlist : playlists)
		{
			if(playlist.getItemId() == playlistId)
			{
				return playlist;
			}
		}

		return null;
	}

}
