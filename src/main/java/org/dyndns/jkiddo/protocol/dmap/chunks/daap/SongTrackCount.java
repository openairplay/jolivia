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

package org.dyndns.jkiddo.protocol.dmap.chunks.daap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UShortChunk;

/**
 * The total number of Tracks.
 * 
 * @author Roger Kapsi
 */
public class SongTrackCount extends UShortChunk
{

	/**
	 * Creates a new SongTrackCount with 0 tracks. You can change this value with {@see #setValue(int)}.
	 */
	public SongTrackCount()
	{
		this(0);
	}

	/**
	 * Creates a new SongTrackCount with the assigned count. You can change this value with {@see #setValue(int)}.
	 * 
	 * @param <tt>count</tt> the count of tracks the album has where this song belongs to.
	 */
	public SongTrackCount(int count)
	{
		super("astc", "daap.songtrackcount", count);
	}
}
