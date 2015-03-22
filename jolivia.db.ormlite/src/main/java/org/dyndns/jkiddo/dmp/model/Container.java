package org.dyndns.jkiddo.dmp.model;
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


import java.util.Collection;

import org.dyndns.jkiddo.dmp.IContainer;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "containers")
public class Container implements IContainer
{
	@ForeignCollectionField()
	private Collection<MediaItem> mediaItems;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	private Database database;

	@DatabaseField(generatedId = true, columnName = "dmap.itemid")
	private int itemId;

	@DatabaseField(columnName = "daap.baseplaylist")
	private int audioBasePlayList;

	@DatabaseField(columnName = "dmap.itemname")
	private String itemName;

	public Container()
	{}

	public Container(String itemName, long persistentId, long itemId, Database database)
	{
		this(itemName, persistentId, itemId);
		this.database = database;
	}
	
	public Container(String itemName, long persistentId, long itemId, Database database, int audioBasePlaylist)
	{
		this(itemName, persistentId, itemId, database);
		this.audioBasePlayList = audioBasePlaylist;
	}

	public int getAudioBasePlayList()
	{
		return audioBasePlayList;
	}

	public Container(String itemName, long persistentId, long itemId)
	{
		this.itemName = itemName;
		this.mediaItems = Sets.newHashSet();
	}

	public void addMediaItem(MediaItem mediaItem)
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
