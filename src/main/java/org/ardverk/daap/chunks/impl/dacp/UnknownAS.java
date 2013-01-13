package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UIntChunk;

public class UnknownAS extends UIntChunk
{
	public UnknownAS()
	{
		this(0);
	}

	public UnknownAS(int value)
	{
		super("caas", "com.apple.itunes.unknown-as", value);
	}

}
