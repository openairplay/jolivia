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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mediaitems")
public class MediaItem
{
	public MediaItem()
	{}

	public MediaItem(final Database database)
	{
		setDatabase(database);
	}
	
	public void setDatabase(final Database database)
	{
		this.database = database;
	}
	
	@DatabaseField(foreignAutoRefresh = true, foreign = true)
	private Container container;
	
	@DatabaseField(canBeNull = false)
	private String externalIdentifer;

	public String getExternalIdentifer()
	{
		return externalIdentifer;
	}

	public void setExternalIdentifer(final String externalIdentifer)
	{
		this.externalIdentifer = externalIdentifer;
	}

	@DatabaseField(generatedId = true, columnName = "dmap.itemid")
	private int itemId;

	@DatabaseField(columnName = "dmap.itemkind", canBeNull = false)
	private int itemKind;

	@DatabaseField(columnName = "daap.songalbum")
	private String songAlbum;

	@DatabaseField(columnName = "daap.songartist")
	private String songArtist;

	@DatabaseField(columnName = "dmap.itemname")
	private String itemName;

	@DatabaseField(columnName = "daap.songformat")
	private String mediaFormat;

	@DatabaseField(columnName = "daap.songsamplerate")
	private int songSampleRate;

	@DatabaseField(columnName = "daap.songtime")
	private int songTime;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, canBeNull = false)
	private Database database;

	public void setItemKind(final int value)
	{
		this.itemKind = value;
	}

	public void setSongAlbum(final String value)
	{
		this.songAlbum = value;
	}

	public void setSongArtist(final String value)
	{
		this.songArtist = value;
	}

	public void setItemName(final String value)
	{
		this.itemName = value;
	}

	public void setMediaFormat(final String value)
	{
		this.mediaFormat = value;
	}

	public void setSongSampleRate(final int value)
	{
		this.songSampleRate = value;
	}

	public void setSongTime(final int value)
	{
		this.songTime = value;
	}

	public int getItemId()
	{
		return itemId;
	}

	public String getSongAlbum()
	{
		return songAlbum;
	}

	public String getSongArtist()
	{
		return songArtist;
	}

	public String getItemName()
	{
		return itemName;
	}

	public String getMediaFormat()
	{
		return mediaFormat;
	}

	public int getSongSampleRate()
	{
		return songSampleRate;
	}

	public int getSongTime()
	{
		return songTime;
	}
	
}