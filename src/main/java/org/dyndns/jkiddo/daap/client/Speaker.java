/*
    TunesRemote+ - http://code.google.com/p/tunesremote-plus/
    
    Copyright (C) 2008 Jeffrey Sharkey, http://jsharkey.org/
    Copyright (C) 2010 TunesRemote+, http://code.google.com/p/tunesremote-plus/
    Copyright (C) 2011 Daniel Thommes
    
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
 * Representation of a speaker as can be constructed from the response of the <code>getspeakers</code> DACP call
 * 
 * @author Daniel Thommes
 */
public class Speaker
{

	/**
	 * ID of the speaker, the computer speaker typically has ID 0. Tag in DACP response: <code>msma</code>
	 */
	private long id;
	/**
	 * Name of the speaker. Tag in DACP response: <code>minm</code>
	 */
	private String name;
	/**
	 * Flag indicating that the speaker is activated. Tag in DACP response: <code>caia</code>
	 */
	private boolean active;
	/**
	 * Computed volume that is the product of the speakers relative volume and the master iTunes-Volume.
	 */
	private int absoluteVolume;

	public int getAbsoluteVolume()
	{
		return absoluteVolume;
	}

	public void setAbsoluteVolume(int absoluteVolume)
	{
		this.absoluteVolume = absoluteVolume;
	}

	public boolean isActive()
	{
		return active;
	}

	public boolean isLocalSpeaker()
	{
		return id == 0;
	}

	public long getId()
	{
		return id;
	}

	/**
	 * @return The ID as hex string e.g. <code>0xFED123</code>
	 */
	public String getIdAsHex()
	{
		return "0x" + Long.toHexString(id);
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "Speaker [id=" + id + ", name=" + name + ", active=" + active + ", absoluteVolume=" + absoluteVolume + "]";
	}

}
