/*
    TunesRemote+ - http://code.google.com/p/tunesremote-plus/
    
    Copyright (C) 2008 Jeffrey Sharkey, http://jsharkey.org/
    Copyright (C) 2010 TunesRemote+, http://code.google.com/p/tunesremote-plus/
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    The Initial Developer of the Original Code is Jeffrey Sharkey.
    Portions created by Jeffrey Sharkey are
    Copyright (C) 2008. Jeffrey Sharkey, http://jsharkey.org/
    All Rights Reserved.
 */
package org.dyndns.jkiddo.daap.client;

/**
 * POJO representing a Playlist object.
 */
public class Playlist
{
	private final long ID;
	private final String name, persistentId;
	private final long count;

	public Playlist(final long ID, final String name, final long count, final String persistentId)
	{
		this.ID = ID;
		this.name = name;
		this.count = count;
		this.persistentId = persistentId;
	}

	/**
	 * Gets the iD.
	 * <p>
	 * 
	 * @return Returns the iD.
	 */
	public long getID()
	{
		return ID;
	}

	/**
	 * Gets the name.
	 * <p>
	 * 
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the persistentId.
	 * <p>
	 * 
	 * @return Returns the persistentId.
	 */
	public String getPersistentId()
	{
		return persistentId;
	}

	/**
	 * Gets the count.
	 * <p>
	 * 
	 * @return Returns the count.
	 */
	public long getCount()
	{
		return count;
	}
}
