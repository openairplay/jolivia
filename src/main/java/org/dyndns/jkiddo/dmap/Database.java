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

package org.dyndns.jkiddo.dmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A Database is a container for Playlists and it keeps track of all Songs in the Database whereat it is not responsible for the actual management of the Songs (it's only interested in the Song IDs).
 * 
 * @author Roger Kapsi
 */
public class Database
{

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
	private final List<Container> playlists = new ArrayList<Container>();

	/** Set of deleted playlists */
	private Set<Container> deletedPlaylists = null;

	/** Set of deleted Songs */
	private Set<Item> deletedSongs = null;

	/** master playlist */
	private Container masterPlaylist = null;

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

		Set<Item> songs = database.getItems();

		for(Container playlist : database.playlists)
		{
			if(txn.modified(playlist))
			{
				if(deletedPlaylists == null || !deletedPlaylists.contains(playlist))
				{
					Container clone = new Container(playlist, txn);
					playlists.add(clone);

					if(playlist == database.masterPlaylist)
					{
						this.masterPlaylist = clone;
					}
				}

				Set<Item> deletedSongs = playlist.getDeletedSongs();
				if(deletedSongs != null && !deletedSongs.isEmpty())
				{
					if(this.deletedSongs == null)
					{
						this.deletedSongs = new HashSet<Item>(deletedSongs);
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
		this(name, new Container(name));
	}

	/**
	 * Create a new Database with the name
	 * 
	 * @param name
	 *            a name for this Database
	 */
	public Database(String name, Container masterPlaylist)
	{
		this(name, DATABASE_ID.getAndIncrement(), Library.nextPersistentId(), masterPlaylist);
	}

	public Database(String name, long itemId, long persistentId)
	{
		this(name, itemId, persistentId, new Container(name));
	}

	public Database(String name, long itemId, long persistentId, Container playlist)
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
	public Container getMasterPlaylist()
	{
		return masterPlaylist;
	}

	/**
	 * Returns an unmodifiable Set with all Playlists in this Database
	 * 
	 * @return unmodifiable Set of Playlists
	 */
	public Collection<Container> getContainers()
	{
		return Collections.unmodifiableList(playlists);
	}

	public void addPlaylists(Transaction txn, final Collection<Container> playlists)
	{
		for(Container p : playlists)
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
	public void addPlaylist(Transaction txn, final Container playlist)
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

	private void addPlaylistP(Transaction txn, Container playlist)
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
	public void removePlaylist(Transaction txn, final Container playlist)
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

	private void removePlaylistP(Transaction txn, Container playlist)
	{
		if(playlists.remove(playlist))
		{
			totalPlaylistCount = playlists.size();

			if(deletedPlaylists == null)
			{
				deletedPlaylists = new HashSet<Container>();
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
	public boolean containsPlaylist(Container playlist)
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
	public Set<Item> getItems()
	{
		Set<Item> songs = null;
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				if(songs == null)
				{
					songs = new HashSet<Item>(playlist.getItems());
				}
				else
				{
					songs.addAll(playlist.getItems());
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
		return getItems().size();
	}

	/**
	 * Returns true if song is in this Database
	 */
	public boolean containsSong(Item song)
	{
		for(Container playlist : playlists)
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
	public void addSong(Transaction txn, Item song)
	{
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				playlist.addSong(txn, song);
			}
		}
	}

	public void setSongs(Transaction txn, Collection<Item> songs)
	{
		for(Container playlist : playlists)
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
	public void removeSong(Transaction txn, Item song)
	{
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				playlist.removeSong(txn, song);
			}
		}
	}

	public Set<Container> getSongPlaylists(Item song)
	{
		Set<Container> ret = null;
		for(Container playlist : playlists)
		{
			if(playlist.containsSong(song))
			{
				if(ret == null)
				{
					ret = new HashSet<Container>();
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
	public Item getSong(long itemId)
	{
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				Item song = playlist.getSong(itemId);
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
	public Container getPlaylist(long playlistId)
	{
		for(Container playlist : playlists)
		{
			if(playlist.getItemId() == playlistId)
			{
				return playlist;
			}
		}

		return null;
	}

}
