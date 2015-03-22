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

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "containers")
public class Container
{
	@ForeignCollectionField()
	private Collection<MediaItem> mediaItems;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	private Database database;

	@DatabaseField(generatedId = true, columnName = "dmap.itemid")
	private long itemId;

	@DatabaseField(columnName = "daap.baseplaylist")
	private int audioBasePlayList;

	@DatabaseField(columnName = "dmap.itemname")
	private String itemName;

	public Container()
	{}

	public Container(final String itemName, final long persistentId, final long itemId, final Database database)
	{
		this(itemName, persistentId, itemId);
		this.database = database;
	}
	
	public Container(final String itemName, final long persistentId, final long itemId, final Database database, final int audioBasePlaylist)
	{
		this(itemName, persistentId, itemId, database);
		this.audioBasePlayList = audioBasePlaylist;
	}

	public int getAudioBasePlayList()
	{
		return audioBasePlayList;
	}

	public Container(final String itemName, final long persistentId, final long itemId)
	{
		this.itemName = itemName;
		this.itemId = itemId;
		this.mediaItems = Sets.newHashSet();
	}

	public Container(final String itemName, final long persistentId, final long itemId, final int audioBasePlaylist)
	{
		this(itemName, persistentId, itemId);
		this.audioBasePlayList = audioBasePlaylist;
	}

	public void addMediaItem(final MediaItem mediaItem)
	{
		mediaItems.add(mediaItem);
	}
	public ImmutableCollection<MediaItem> getMediaItems()
	{
		return ImmutableList.copyOf(mediaItems);
	}
	public long getItemId()
	{
		return itemId;
	}
}
