package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UByteChunk;

public class UnknownFE extends UByteChunk
{
	public UnknownFE()
	{
		this(0);
	}

	public UnknownFE(int value)
	{
		super("cafe", "com.apple.itunes.unknown-fe", value);
	}

}
