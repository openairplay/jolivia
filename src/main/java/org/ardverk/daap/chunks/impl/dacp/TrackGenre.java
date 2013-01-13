package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.StringChunk;

public class TrackGenre extends StringChunk
{
	public TrackGenre()
	{
		this(null);
	}

	public TrackGenre(String value)
	{
		super("cang", "com.apple.itunes.unknown-ng", value);
	}

}
