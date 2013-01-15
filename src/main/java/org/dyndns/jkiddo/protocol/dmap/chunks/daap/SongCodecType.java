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

import org.dyndns.jkiddo.protocol.dmap.ByteUtil;
import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

/**
 * Unknown purpose.
 * 
 * @author Roger Kapsi
 * @since iTunes 4.5
 */
public class SongCodecType extends UIntChunk
{

	public static final int MPEG = ByteUtil.toFourCharCode("mpeg");
	public static final int MP4A = ByteUtil.toFourCharCode("mp4a");

	/** Creates a new instance of SongCodecType */
	public SongCodecType()
	{
		this(0);
	}

	public SongCodecType(long codec)
	{
		super("ascd", "daap.songcodectype", codec);
	}
}
