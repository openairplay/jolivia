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
 * The artist of the Song.
 * 
 * @author Roger Kapsi
 */
public class SongArtist extends StringChunk
{

	/**
	 * Creates a new SongArtist where Artist is <tt>null</tt> (i.e. no artist)
	 */
	public SongArtist()
	{
		this(null);
	}

	/**
	 * Creates a new SongArtist with the secified artist name.
	 * 
	 * @param artist
	 *            the name of the artist (individual, a band or group)
	 */
	public SongArtist(String artist)
	{
		super("asar", "daap.songartist", artist);
	}
}
