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

package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.BooleanChunk;

/**
 * Enables or disables the Song. Default is enabled. iTunes shows this as the small checkbox next to the Song name.
 * 
 * @author Roger Kapsi
 */
public class SongDisabled extends BooleanChunk
{

	/**
	 * Creates a new SongDisabled where song is enabled. You can change this value with {@see #setValue(boolean)}.
	 */
	public SongDisabled()
	{
		this(false);
	}

	/**
	 * Creates a new SongDisabled with the assigned value. You can change this value with {@see #setValue(boolean)}.
	 * 
	 * @param <tt>disabled</tt> enables or disables this song.
	 */
	public SongDisabled(boolean disabled)
	{
		super("asdb", "daap.songdisabled", disabled);
	}
}
