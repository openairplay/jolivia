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


import org.dyndns.jkiddo.dmp.IContainer;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mediaitems")
public class MediaItem
{
	public MediaItem()
	{}

	public MediaItem(Database database)
	{
		setDatabase(database);
	}
	
	public void setDatabase(Database database)
	{
		this.database = database;
		container = this.database.getMasterContainer();
	}
	
	@DatabaseField(canBeNull = false)
	private String externalIdentifer;

	public String getExternalIdentifer()
	{
		return externalIdentifer;
	}

	public void setExternalIdentifer(String externalIdentifer)
	{
		this.externalIdentifer = externalIdentifer;
	}
	
	private static final String name =IDmapProtocolDefinition.DmapProtocolDefinition.miid.longname;

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

	@DatabaseField(/*columnName="dmap.containeritemid",*/ foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	private IContainer container;

	public void setItemKind(int value)
	{
		this.itemKind = value;
	}

	public void setSongAlbum(String value)
	{
		this.songAlbum = value;
	}

	public void setSongArtist(String value)
	{
		this.songArtist = value;
	}

	public void setItemName(String value)
	{
		this.itemName = value;
	}

	public void setMediaFormat(String value)
	{
		this.mediaFormat = value;
	}

	public void setSongSampleRate(int value)
	{
		this.songSampleRate = value;
	}

	public void setSongTime(int value)
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