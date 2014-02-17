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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.dyndns.jkiddo.dmp.chunks.Chunk;
import org.dyndns.jkiddo.dmp.chunks.media.ContainerItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.PersistentId;

/**
 * There isn't much to say: a Song is a Song.
 * <p>
 * Note: although already mentioned in StringChunk I'd like to point out that <code>null</code> is a valid value for DAAP. Use it to reset Strings. See StringChunk for more information!
 * </p>
 * 
 * @author Roger Kapsi
 */
public class MediaItem
{

	/** songId is an 32bit unsigned value! */
	private static final AtomicLong MEDIA_ITEM_ID = new AtomicLong(1024);

	private final Map<String, Chunk> chunks = new HashMap<String, Chunk>();

	private final ItemKind itemKind;
	private final ItemId itemId = new ItemId(MEDIA_ITEM_ID.getAndIncrement());
	private final ItemName itemName = new ItemName();
	private final ContainerItemId containerItemId = new ContainerItemId();
	private final PersistentId persistentId = new PersistentId();

	/**
	 * Creates a new Song
	 */
	public MediaItem(ItemKind itemKind)
	{
		this.itemKind = itemKind;
		persistentId.setValue(itemId.getValue());
		containerItemId.setValue(itemId.getValue());
		init();
	}

	/*
	 * public Item() { this.itemKind = new ItemKind(ItemKind.AUDIO); persistentId.setValue(itemId.getValue()); containerItemId.setValue(itemId.getValue()); init(); }
	 */

	/**
	 * Creates a new Song with the provided name
	 */
	/*
	 * public MediaItem(String name) { this(); itemName.setValue(name); }
	 */

	private void init()
	{
		addChunk(itemKind);
		addChunk(itemName);
		addChunk(itemId);
		addChunk(containerItemId);

		// Some clients do not init format (implicit mp3)
		// and use uninitialized garbage instead
//		addChunk(FORMAT);

		// VLC requires the sample rate
//		addChunk(SAMPLE_RATE);

		// Added as part of debug
		// addChunk(new SongDisabled(true));
		// addChunk(new AlbumArtist());
		// addChunk(new EMediaKind(1));
		// addChunk(new DownloadStatus(true));

	}

	/**
	 * Returns the unique id of this song
	 */
	public long getItemId()
	{
		return itemId.getUnsignedValue();
	}

	/**
	 * Returns the id of this Songs container. Note: same as getId()
	 */
	protected long getContainerId()
	{
		return containerItemId.getUnsignedValue();
	}

	public void addChunk(Chunk chunk)
	{
		if(chunk != null)
		{
			chunks.put(chunk.getName(), chunk);
		}
	}

	public Chunk getChunk(String name)
	{
		return chunks.get(name);
	}

	public Collection<Chunk> getChunks()
	{
		return Collections.unmodifiableCollection(chunks.values());
	}

	@Override
	public int hashCode()
	{
		return (int) getItemId();
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof MediaItem))
		{
			return false;
		}

		return ((MediaItem) o).getItemId() == getItemId();
	}

}
