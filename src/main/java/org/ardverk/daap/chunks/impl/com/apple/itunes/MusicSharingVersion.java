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

import org.ardverk.daap.chunks.UIntChunk;
import org.ardverk.daap.chunks.VersionChunk;

/**
 * Unknown purpose. Is for some reason derivated from signed int. Should be a VersionChunk!
 * 
 * @author Roger Kapsi
 */
public class MusicSharingVersion extends UIntChunk
{

	/** Creates a new instance of MusicSharingVersion */
	public MusicSharingVersion()
	{
		this(0);
	}

	public MusicSharingVersion(long sharingVersion)
	{
		super("aeSV", "com.apple.itunes.music-sharing-version", sharingVersion);
	}

	public int getMajorVersion()
	{
		return VersionChunk.getMajorVersion(getValue());
	}

	public int getMinorVersion()
	{
		return VersionChunk.getMinorVersion(getValue());
	}

	public int getMicroVersion()
	{
		return VersionChunk.getMicroVersion(getValue());
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + contentCode + "; uint)=" + getMajorVersion() + "." + getMinorVersion() + "." + getMicroVersion();
	}
}
