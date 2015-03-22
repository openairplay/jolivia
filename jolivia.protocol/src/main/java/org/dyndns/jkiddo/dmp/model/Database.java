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

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "databases")
public class Database
{
	public int getItemId()
	{
		return itemId;
	}

	public Database()
	{}

	public Database(final String databaseName, final String type, final Library library)
	{
		this.name = databaseName;
		this.type = type;
		this.library = library;
		this.containers = Sets.newHashSet();
	}

	public Database(final String databaseName, final int itemId, final long persistentId, final Container masterPlaylist)
	{
		this(databaseName, itemId, persistentId);
		this.containers = Sets.newHashSet();
		this.containers.add(masterPlaylist);
		this.mediaItems = masterPlaylist.getMediaItems();
	}

	public Database(final String databaseName, final int itemId, final long persistentId)
	{
		this.name = databaseName;
		this.itemId = itemId;
		this.containers = Sets.newHashSet();
	}

	@DatabaseField(id = true)
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

	public Container getContainer(final long containerId)
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

	public Container getMasterContainer()
	{
		return FluentIterable.from(containers).firstMatch(new Predicate<Container>() {

			@Override
			public boolean apply(final Container input)
			{
				return input.getAudioBasePlayList() == 1 ? true : false;
			}
		}).get();
	}

	public Collection<Container> getContainers()
	{
		return ImmutableList.copyOf(containers);
	}
}
