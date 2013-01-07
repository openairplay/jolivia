package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UByteChunk;

public class ShuffleStatus extends UByteChunk
{
	public ShuffleStatus()
	{
		this(0);
	}

	public ShuffleStatus(int value)
	{
		super("cash", "com.apple.itunes.unknown-sh", value);
	}

}
