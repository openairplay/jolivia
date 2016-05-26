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
import org.dyndns.jkiddo.dmp.IDatabase;
import org.dyndns.jkiddo.dmp.IMediaItem;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "databases")
public class Database implements IDatabase
{

	@DatabaseField(generatedId = true)
	private int itemId;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	private Library library;

	@DatabaseField(columnName = "dmap.itemname")
	private String name;

	@DatabaseField
	private String type;

	@ForeignCollectionField(eager = true)
	private Collection<Container> containers;

	@ForeignCollectionField()
	private Collection<MediaItem> mediaItems;

	public Database()
	{}

	public Database(String databaseName, String type, Library library)
	{
		this.name = databaseName;
		this.type = type;
		this.library = library;
		this.containers = Sets.newHashSet();
	}

	public Database(String databaseName, int itemId, long persistentId, Container playlist)
	{
		throw new RuntimeException("Not implemented");
	}

	public Database(String databaseName, int itemId, long persistentId)
	{
		this.name = databaseName;
		this.itemId = itemId;
	}

	public long getItemId()
	{
		return itemId;
	}

	public Container getContainer(long containerId)
	{
		throw new RuntimeException("Not implemented");
	}

	public String getName()
	{
		return name;
	}

	/*
	 * public long getMediaItemCount() { return getMasterContainer().getMediaItems().size(); }
	 */
	/*
	 * public long getContainersCount() { return getContainers().size(); }
	 */

	public long getPersistentId()
	{
		return 0;
	}

	public ImmutableCollection<MediaItem> getMediaItems()
	{
		return ImmutableList.copyOf(mediaItems);
	}

	public IContainer getMasterContainer()
	{
		return FluentIterable.from(containers).firstMatch(new Predicate<Container>() {

			@Override
			public boolean apply(Container input)
			{
				return input.getAudioBasePlayList() == 1 ? true : false;
			}
		}).get();
	}

	public Collection<IContainer> getContainers()
	{
		return containers;
		return ImmutableList.copyOf(containers);
	}

	@Override
	public Collection<IMediaItem> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSongCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMediaItems(Collection<IMediaItem> songs) {
		// TODO Auto-generated method stub
		
	}
}
