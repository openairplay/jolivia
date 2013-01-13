package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UIntChunk;

public class UnknownHI extends UIntChunk
{
	public UnknownHI()
	{
		this(0);
	}

	public UnknownHI(int value)
	{
		super("mshi", "com.apple.itunes.unknown-hi", value);
	}
}
