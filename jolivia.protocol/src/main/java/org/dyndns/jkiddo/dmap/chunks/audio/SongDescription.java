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

package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

/**
 * This is the description of the Song format and not of the song! For example is the description of a MP3 file 'MPEG audio file'.
 * 
 * @author Roger Kapsi
 */
public class SongDescription extends StringChunk
{
	/**
	 * Description for a MPEG Audio Layer 3 file (MP3)
	 */
	public static final String MPEG_AUDIO_FILE = "MPEG audio file";

	/**
	 * Description for a Audio Interchange File Format file (AIFF)
	 */
	public static final String AIFF_AUDIO_FILE = "AIFF audio file";

	/**
	 * Description for a MPEG4 Advanced Audio Coding file (AAC)
	 */
	public static final String AAC_AUDIO_FILE = "AAC audio file";

	/**
	 * Description for a WAV file (WAV)
	 */
	public static final String WAV_AUDIO_FILE = "WAV audio file";

	/**
	 * Description for a Playlist URL
	 */
	public static final String PLAYLIST_URL = "Playlist URL";
	
	/**
	 * Description for a MPEG4 Advanced Audio Coding file (AAC)
	 */
	public static final String PURCHASED_AAC_AUDIO_FILE = "Purchased " + AAC_AUDIO_FILE;

	/**
	 * Creates a new SongDescription where no description is set. You can change this value with {@see #setValue(String)}.
	 */
	public SongDescription()
	{
		this(null);
	}

	/**
	 * Creates a new SongDescription with the assigned description. You can change this value with {@see #setValue(String)}.
	 * 
	 * @param <tt>description</tt> the description of the format of this song.
	 */
	public SongDescription(String description)
	{
		super("asdt", "daap.songdescription", description);
	}
}
