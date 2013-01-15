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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.dyndns.jkiddo.protocol.dmap.chunks.BooleanChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.Chunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.com.apple.itunes.PodcastPlaylist;
import org.dyndns.jkiddo.protocol.dmap.chunks.com.apple.itunes.SmartPlaylist;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.PlaylistRepeatMode;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.PlaylistShuffleMode;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemCount;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemId;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemName;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.PersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The name is self explaining. A Playlist is a set of Songs.
 * 
 * @author Roger Kapsi
 */
public class Playlist
{

	private static final Logger LOG = LoggerFactory.getLogger(Playlist.class);

	/** playlistId is an 32bit unsigned value! */
	private static final AtomicLong PLAYLIST_ID = new AtomicLong(1);

	/** unique id */
	private final ItemId itemId = new ItemId(PLAYLIST_ID.getAndIncrement());

	/** unique persistent id */
	private final PersistentId persistentId = new PersistentId();

	/** The name of playlist */
	private final ItemName itemName = new ItemName();

	/** the total number of songs in this playlist */
	private final ItemCount itemCount = new ItemCount();

	private SmartPlaylist smartPlaylist;

	// @since iTunes 5.0
	private PlaylistRepeatMode repeatMode;
	private PlaylistShuffleMode shuffleMode;
	private PodcastPlaylist podcastPlaylist;
	// private HasChildContainers hasChildContainers;

	/** */
	private final Map<String, Chunk> chunks = new HashMap<String, Chunk>();

	/** Set of Songs */
	private final List<Song> songs = new ArrayList<Song>();

	/** Set of deleted Songs */
	private Set<Song> deletedSongs = null;

	public Playlist(Playlist playlist, Transaction txn)
	{
		this.itemId.setValue(playlist.itemId.getValue());
		this.itemName.setValue(playlist.itemName.getValue());
		this.persistentId.setValue(playlist.persistentId.getValue());
		this.itemCount.setValue(playlist.itemCount.getValue());

		if(playlist.deletedSongs != null)
		{
			this.deletedSongs = playlist.deletedSongs;
			playlist.deletedSongs = null;
		}

		for(Song song : playlist.songs)
		{
			if(txn.modified(song))
			{
				if(deletedSongs == null || !deletedSongs.contains(song))
				{
					// cloning is not necessary
					// songs.add(new Song(song));
					songs.add(song);
				}
			}
		}

		init();
	}

	/**
	 * Creates a new Playlist
	 * 
	 * @param name
	 *            the Name of the Playlist
	 */
	public Playlist(String name)
	{
		this.itemName.setValue(name);
		this.persistentId.setValue(Library.nextPersistentId());

		init();
	}

	public Playlist(String name, long persistentId, long itemId, long itemCount)
	{
		this.itemName.setValue(name);
		this.persistentId.setValue(persistentId);
		this.itemId.setValue(itemId);
		this.itemCount.setValue(itemCount);
		init();
	}

	private void init()
	{
		addChunk(itemId);
		addChunk(itemName);
		addChunk(persistentId);
		addChunk(itemCount);
	}

	/**
	 * Returns the unique ID of this Playlist
	 * 
	 * @return unique ID of this Playlist
	 */
	public long getItemId()
	{
		return itemId.getUnsignedValue();
	}

	/**
	 * @param chunk
	 */
	protected void addChunk(Chunk chunk)
	{
		chunks.put(chunk.getName(), chunk);
	}

	protected void removeChunk(Chunk chunk)
	{
		chunks.remove(chunk.getName());
	}

	public Chunk getChunk(String name)
	{
		return chunks.get(name);
	}

	/**
	 * Sets the name of this Playlist
	 * 
	 * @param txn
	 *            a Transaction
	 * @param name
	 *            a new name
	 * @throws DmapException
	 */
	public void setName(Transaction txn, String name)
	{
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn("itemName", name));
		}
		else
		{
			setValue("itemName", name);
		}
	}

	/**
	 * Returns the name of this Playlist
	 * 
	 * @return the name of this Playlist
	 */
	public String getName()
	{
		return getStringValue(itemName);
	}

	/**
	 * Sets whether or not this Playlist is a smart playlist. The difference between smart and common playlists is that smart playlists have a star as an icon and they appear as first in the list.
	 */
	public void setSmartPlaylist(Transaction txn, boolean smartPlaylist)
	{
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn("smartPlaylist", smartPlaylist));
		}
		else
		{
			setValue("smartPlaylist", smartPlaylist);
		}
	}

	/**
	 * Returns <code>true</code> if this Playlist is a smart playlist.
	 */
	public boolean isSmartPlaylist()
	{
		return getBooleanValue(smartPlaylist);
	}

	/**
     *
     */
	public void setPodcastPlaylist(Transaction txn, final boolean podcastPlaylist)
	{
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn("podcastPlaylist", podcastPlaylist));
		}
		else
		{
			setValue("podcastPlaylist", podcastPlaylist);
		}
	}

	/**
     *
     */
	public boolean isPodcastPlaylist()
	{
		return getBooleanValue(podcastPlaylist);
	}

	/**
     *
     */
	public void setRepeatMode(Transaction txn, int repeatMode)
	{
		UByteChunk.checkUByteRange(repeatMode);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn("repeatMode", repeatMode));
		}
		else
		{
			setValue("repeatMode", repeatMode);
		}
	}

	/**
     *
     */
	public int getRepeatMode()
	{
		return getUByteValue(repeatMode);
	}

	/**
     *
     */
	public void setShuffleMode(Transaction txn, int shuffleMode)
	{
		UByteChunk.checkUByteRange(shuffleMode);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn("shuffleMode", shuffleMode));
		}
		else
		{
			setValue("shuffleMode", shuffleMode);
		}
	}

	/**
     *
     */
	public int getShuffleMode()
	{
		return getUByteValue(shuffleMode);
	}

	/**
	 * Returns the number of Songs in this Playlist
	 */
	public int getSongCount()
	{
		return songs.size();
	}

	/**
	 * Retuns an unmodifiable set of all songs
	 */
	public List<Song> getSongs()
	{
		return Collections.unmodifiableList(songs);
	}

	/**
	 * Returns a set of deleted Songs or null if no Songs were removed from this Playlist
	 */
	public Set<Song> getDeletedSongs()
	{
		return deletedSongs;
	}

	/**
	 * Adds <code>song</code> to this Playlist
	 * 
	 * @param song
	 * @throws DaapTransactionException
	 */
	public void addSong(Transaction txn, final Song song)
	{
		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					addSongP(txn, song);
				}
			});
			txn.attach(song); //
		}
		else
		{
			addSongP(txn, song);
		}
	}

	private void addSongP(Transaction txn, Song song)
	{
		if(!containsSong(song) && songs.add(song))
		{
			itemCount.setValue(songs.size());
			if(deletedSongs != null && deletedSongs.remove(song) && deletedSongs.isEmpty())
			{
				deletedSongs = null;
			}
		}
	}

	public void setSongs(Transaction txn, final Collection<Song> songs)
	{
		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					setSongsP(txn, songs);
				}
			});
			txn.attach(songs); //
		}
		else
		{
			setSongsP(txn, songs);
		}
	}

	private void setSongsP(Transaction txn, Collection<Song> songsCollection)
	{
		this.songs.addAll(songsCollection);
		itemCount.setValue(songsCollection.size());
	}

	/**
	 * Removes <code>song</code> from this Playlist
	 * 
	 * @param song
	 * @throws DaapTransactionException
	 */
	public void removeSong(Transaction txn, final Song song)
	{
		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					removeSongP(txn, song);
				}
			});
		}
		else
		{
			removeSongP(txn, song);
		}
	}

	private void removeSongP(Transaction txn, Song song)
	{
		if(songs.remove(song))
		{
			itemCount.setValue(songs.size());
			if(deletedSongs == null)
			{
				deletedSongs = new HashSet<Song>();
			}
			deletedSongs.add(song);
		}
	}

	/**
	 * Gets and returns a Song by its ID
	 * 
	 * @param songId
	 * @return
	 */
	public Song getSong(String itemId)
	{
		long songId = Long.parseLong(itemId);
		for(Song song : songs)
		{
			if(song.getItemId() == songId)
			{
				return song;
			}
		}
		return null;
	}

	/**
	 * Returns <code>true</code> if the provided <code>song</code> is in this Playlist.
	 */
	public boolean containsSong(Song song)
	{
		return songs.contains(song);
	}

	@Override
	public int hashCode()
	{
		return itemId.getValue();
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Playlist))
		{
			return false;
		}

		return ((Playlist) o).getItemId() == getItemId();
	}

	@Override
	public String toString()
	{
		return getName() + " (" + getItemId() + ")";
	}

	protected boolean getBooleanValue(BooleanChunk chunk)
	{
		return (chunk != null) ? chunk.getBooleanValue() : false;
	}

	protected int getUByteValue(UByteChunk chunk)
	{
		return (chunk != null) ? chunk.getValue() : 0;
	}

	protected String getStringValue(StringChunk chunk)
	{
		return (chunk != null) ? chunk.getValue() : null;
	}

	protected Txn createNewTxn(final String name, boolean value)
	{
		return createNewTxn(name, boolean.class, new Boolean(value));
	}

	protected Txn createNewTxn(final String name, int value)
	{
		return createNewTxn(name, int.class, new Integer(value));
	}

	protected Txn createNewTxn(final String name, long value)
	{
		return createNewTxn(name, long.class, new Long(value));
	}

	protected Txn createNewTxn(final String name, String value)
	{
		return createNewTxn(name, String.class, value);
	}

	protected Txn createNewTxn(final String fieldName, final Class<?> valueClass, final Object value)
	{
		Txn txn = new Txn() {
			@Override
			public void commit(Transaction txn)
			{
				setValue(fieldName, valueClass, value);
			}
		};

		return txn;
	}

	protected void setValue(String fieldName, boolean value)
	{
		setValue(fieldName, boolean.class, new Boolean(value));
	}

	protected void setValue(String fieldName, int value)
	{
		setValue(fieldName, int.class, new Integer(value));
	}

	protected void setValue(String fieldName, long value)
	{
		setValue(fieldName, long.class, new Long(value));
	}

	protected void setValue(String fieldName, String value)
	{
		setValue(fieldName, String.class, value);
	}

	protected void setValue(String fieldName, Class<?> valueClass, Object value)
	{
		try
		{

			Field field = Playlist.class.getDeclaredField(fieldName);
			field.setAccessible(true);

			Chunk chunk = (Chunk) field.get(this);
			if(chunk == null)
			{
				Constructor<?> con = field.getType().getConstructor(new Class[] { valueClass });
				chunk = (Chunk) con.newInstance(new Object[] { value });
				field.set(this, chunk);
				addChunk(chunk);
			}
			else
			{
				Method method = field.getType().getMethod("setValue", new Class[] { valueClass });
				method.invoke(chunk, new Object[] { value });
			}

		}
		catch(SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch(IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch(NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
		catch(NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch(IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch(InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
		catch(InstantiationException e)
		{
			throw new RuntimeException(e);
		}
	}
}
