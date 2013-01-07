package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UByteChunk;

public class RepeatStatus extends UByteChunk
{
	public RepeatStatus()
	{
		this(0);
	}

	public RepeatStatus(int value)
	{
		super("carp", "com.apple.itunes.unknown-rp", value);
	}

}
