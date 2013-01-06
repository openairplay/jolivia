package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.ULongChunk;

public class UnknownRM extends ULongChunk
{

	public UnknownRM()
	{
		this(0);
	}

	public UnknownRM(long value)
	{
		super("aeRM", "com.apple.itunes.unknown-RM", value);
	}
}
