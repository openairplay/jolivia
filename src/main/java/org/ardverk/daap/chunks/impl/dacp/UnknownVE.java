package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UByteChunk;

public class UnknownVE extends UByteChunk
{
	public UnknownVE()
	{
		this(0);
	}

	public UnknownVE(int value)
	{
		super("cave", "com.apple.itunes.unknown-ve", value);
	}
}
