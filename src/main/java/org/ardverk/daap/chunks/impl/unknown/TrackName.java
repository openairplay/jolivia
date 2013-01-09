package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.StringChunk;

public class TrackName extends StringChunk
{
	public TrackName()
	{
		this(null);
	}

	public TrackName(String value)
	{
		super("cann", "com.apple.itunes.unknown-nn", value);
	}

}
