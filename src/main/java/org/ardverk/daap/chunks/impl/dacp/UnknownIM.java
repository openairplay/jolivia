package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.ULongChunk;

public class UnknownIM extends ULongChunk
{
	public UnknownIM()
	{
		this(0);
	}

	public UnknownIM(long value)
	{
		super("aeIM", "com.apple.itunes.unknown-IM", value);
	}
}
