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

package org.dyndns.jkiddo.dmp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.dyndns.jkiddo.dmap.chunks.audio.BaseContainer;
import org.dyndns.jkiddo.dmp.DmapException;
import org.dyndns.jkiddo.dmp.IDatabase;
import org.dyndns.jkiddo.dmp.chunks.media.EditCommandSupported;
import org.dyndns.jkiddo.dmp.chunks.media.ParentContainerId;

/**
 * A Database is a container for Playlists and it keeps track of all Songs in the Database whereat it is not responsible for the actual management of the Songs (it's only interested in the Song IDs).
 * 
 * @author Roger Kapsi
 */
public class Database implements IDatabase
{

	/** databaseId is an 32bit unsigned value! */
	private static final AtomicLong DATABASE_ID = new AtomicLong(1);

	/** unique id */
	private final long itemId;

	/** unique persistent id */
	private final long persistentId;

	/** Name of this Database */
	private String name;

	/**
	 * The total number of Playlists in this Database
	 */
	@SuppressWarnings("unused")
	private int totalPlaylistCount = 0;

	/**
	 * The total number of Songs in this Database
	 */
	@SuppressWarnings("unused")
	private int totalSongCount = 0;

	/** A List of Playlists */
	private final List<Container> playlists = new ArrayList<Container>();

	/** Set of deleted playlists */
	private Set<Container> deletedPlaylists = null;

	/** Set of deleted Songs */
	private Set<MediaItem> deletedSongs = null;

	/** master playlist */
	private Container masterPlaylist = null;

	protected Database(Database database)
	{
		this.itemId = database.itemId;
		this.persistentId = database.persistentId;
		this.name = database.name;

		if(database.deletedPlaylists != null)
		{
			this.deletedPlaylists = database.deletedPlaylists;
			database.deletedPlaylists = null;
		}

		Collection<MediaItem> songs = database.getItems();

		for(Container playlist : database.playlists)
		{
			//if(txn.modified(playlist))
			{
				if(deletedPlaylists == null || !deletedPlaylists.contains(playlist))
				{
					Container clone = new Container(playlist);
					playlists.add(clone);

					if(playlist == database.masterPlaylist)
					{
						this.masterPlaylist = clone;
					}
				}

				Set<MediaItem> deletedSongs = playlist.getDeletedSongs();
				if(deletedSongs != null && !deletedSongs.isEmpty())
				{
					if(this.deletedSongs == null)
					{
						this.deletedSongs = new HashSet<MediaItem>(deletedSongs);
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
		this.masterPlaylist.addChunk(new BaseContainer(1));
		this.masterPlaylist.addChunk(new ParentContainerId(0));
		this.masterPlaylist.addChunk(new EditCommandSupported(0));
		addPlaylistP(masterPlaylist);
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getItemId()
	 */
	@Override
	public long getItemId()
	{
		return itemId;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#setName(org.dyndns.jkiddo.dmp.Transaction, java.lang.String)
	 */
	public void setName(final String name)
	{
	
			setNameP(name);

		masterPlaylist.setName(name);
	}

	private void setNameP(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getPersistentId()
	 */
	@Override
	public long getPersistentId()
	{
		return persistentId;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getMasterContainer()
	 */
	@Override
	public Container getMasterContainer()
	{
		return masterPlaylist;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getContainers()
	 */
	@Override
	public Collection<Container> getContainers()
	{
		return Collections.unmodifiableList(playlists);
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#addPlaylists(org.dyndns.jkiddo.dmp.Transaction, java.util.Collection)
	 */
	public void addPlaylists(final Collection<Container> playlists)
	{
		for(Container p : playlists)
		{
			addPlaylist(p);
		}
	}
	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#addPlaylist(org.dyndns.jkiddo.dmp.Transaction, org.dyndns.jkiddo.dmp.Container)
	 */
	@Override
	public void addPlaylist(final Container playlist)
	{
		if(masterPlaylist.equals(playlist))
		{
			throw new DmapException("You cannot add the master playlist.");
		}

		{
			addPlaylistP(playlist);
		}
	}

	private void addPlaylistP(Container playlist)
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

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#removePlaylist(org.dyndns.jkiddo.dmp.Transaction, org.dyndns.jkiddo.dmp.Container)
	 */
	public void removePlaylist(final Container playlist)
	{
		if(masterPlaylist.equals(playlist))
		{
			throw new DmapException("You cannot remove the master playlist.");
		}

		{
			removePlaylistP(playlist);
		}
	}

	private void removePlaylistP(Container playlist)
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

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getPlaylistCount()
	 */
	@Override
	public int getPlaylistCount()
	{
		return playlists.size();
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#containsPlaylist(org.dyndns.jkiddo.dmp.Container)
	 */
	public boolean containsPlaylist(Container playlist)
	{
		return playlists.contains(playlist);
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#toString()
	 */
	@Override
	public String toString()
	{
		return "Database(" + getItemId() + ", " + getName() + ")";
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return (int) (getItemId() & Integer.MAX_VALUE);
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Database))
		{
			return false;
		}
		return ((IDatabase) o).getItemId() == getItemId();
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getItems()
	 */
	@Override
	public Collection<MediaItem> getItems()
	{
		Set<MediaItem> songs = null;
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				if(songs == null)
				{
					songs = new HashSet<MediaItem>(playlist.getItems());
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

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getSongCount()
	 */
	@Override
	public int getSongCount()
	{
		return getItems().size();
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#containsSong(org.dyndns.jkiddo.dmp.MediaItem)
	 */
	public boolean containsSong(MediaItem song)
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

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#addSong(org.dyndns.jkiddo.dmp.Transaction, org.dyndns.jkiddo.dmp.MediaItem)
	 */
	public void addSong(MediaItem song)
	{
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				playlist.addSong(song);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#setMediaItems(org.dyndns.jkiddo.dmp.Transaction, java.util.Collection)
	 */
	@Override
	public void setMediaItems(Collection<MediaItem> songs)
	{
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				playlist.setSongs(songs);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#removeSong(org.dyndns.jkiddo.dmp.Transaction, org.dyndns.jkiddo.dmp.MediaItem)
	 */
	public void removeSong(MediaItem song)
	{
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				playlist.removeSong(song);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getSongPlaylists(org.dyndns.jkiddo.dmp.MediaItem)
	 */
	public Set<Container> getSongPlaylists(MediaItem song)
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

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getSong(long)
	 */
	public MediaItem getSong(long itemId)
	{
		for(Container playlist : playlists)
		{
			if(!(playlist instanceof Folder))
			{
				MediaItem song = playlist.getItem(itemId);
				if(song != null)
				{
					return song;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.IDatabase#getContainer(long)
	 */
	@Override
	public Container getContainer(long playlistId)
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
