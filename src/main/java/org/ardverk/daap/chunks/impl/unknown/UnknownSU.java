package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UByteChunk;

public class UnknownSU extends UByteChunk
{
	public UnknownSU()
	{
		this(0);
	}

	public UnknownSU(int value)
	{
		super("casu", "com.apple.itunes.unknown-su", value);
	}
}
