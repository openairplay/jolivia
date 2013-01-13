package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UByteChunk;

public class UnknownQU extends UByteChunk
{
	public UnknownQU()
	{
		this(0);
	}

	public UnknownQU(int value)
	{
		super("ceQu", "com.apple.itunes.unknown-Qu", value);
	}

}
