package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.StringChunk;

public class NowPlaying extends StringChunk
{
	public NowPlaying()
	{
		this(null);
	}

	public NowPlaying(String value)
	{
		super("canp", "com.apple.itunes.unknown-np", value);
	}

}
