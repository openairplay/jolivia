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

package org.ardverk.daap.chunks.impl.daap;

import org.ardverk.daap.chunks.UByteChunk;

/**
 * This class describes if a song is either a Radio stream or DAAP stream. Radio streams have a different icon and and the data is usually streamed from an URL ({@see SongDataUrl}).
 * 
 * @author Roger Kapsi
 */
public class SongDataKind extends UByteChunk
{

	/** Radio stream */
	public static final int RADIO_STREAM = 1;

	/** DAAP stream (default) */
	public static final int DAAP_STREAM = 2;

	/**
	 * Creates a new SongDataKind with DAAP_STREAM as type.
	 */
	public SongDataKind()
	{
		this(DAAP_STREAM);
	}

	public SongDataKind(int kind)
	{
		super("asdk", "daap.songdatakind", kind);
	}
}
