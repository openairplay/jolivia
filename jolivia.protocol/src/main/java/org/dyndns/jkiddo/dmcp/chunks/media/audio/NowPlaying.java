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
package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.RawChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.canp)
public class NowPlaying extends RawChunk
{
	private long databaseId;
	private long playlistId;
	private long containerItemId;
	private long trackId;

	public NowPlaying()
	{
		this(new byte[] {});
	}

	public NowPlaying(byte[] value)
	{
		super("canp", "dacp.nowplayingids", value);

	}

	public final long getDatabaseId()
	{
		return databaseId;
	}

	public final long getPlaylistId()
	{
		return playlistId;
	}

	public final long getContainerItemId()
	{
		return containerItemId;
	}

	public final long getTrackId()
	{
		return trackId;
	}

	@Override
	public void setValue(byte[] value)
	{
		super.setValue(value);
		if(value != null && value.length == 16)
		{
			databaseId = 0;
			databaseId = (value[0] & 0xff) << 24;
			databaseId |= (value[1] & 0xff) << 16;
			databaseId |= (value[2] & 0xff) << 8;
			databaseId |= value[3] & 0xff;

			playlistId = 0;
			playlistId = (value[4] & 0xff) << 24;
			playlistId |= (value[5] & 0xff) << 16;
			playlistId |= (value[6] & 0xff) << 8;
			playlistId |= value[7] & 0xff;

			containerItemId = 0;
			containerItemId = (value[8] & 0xff) << 24;
			containerItemId |= (value[9] & 0xff) << 16;
			containerItemId |= (value[10] & 0xff) << 8;
			containerItemId |= value[11] & 0xff;

			trackId = 0;
			trackId = (value[12] & 0xff) << 24;
			trackId |= (value[13] & 0xff) << 16;
			trackId |= (value[14] & 0xff) << 8;
			trackId |= value[15] & 0xff;
		}
	}
}
