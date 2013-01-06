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

package org.ardverk.daap.chunks.impl.com.apple.itunes;

import org.ardverk.daap.chunks.BooleanChunk;

/**
 * Used by Playlist to mark itself as smart or not smart (default). The only difference is a slightly different icon.
 * 
 * @author Roger Kapsi
 */
public class SmartPlaylist extends BooleanChunk
{

	public SmartPlaylist()
	{
		this(false);
	}

	public SmartPlaylist(boolean smart)
	{
		super("aeSP", "com.apple.itunes.smart-playlist", smart);
	}
}
