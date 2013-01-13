package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.ULongChunk;

public class UnknownMA extends ULongChunk
{
	public UnknownMA()
	{
		this(0);
	}

	public UnknownMA(long value)
	{
		super("msma", "com.apple.itunes.unknown-ma", value);
	}

}
