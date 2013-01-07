package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UIntChunk;

public class UnknownSA extends UIntChunk
{
	public UnknownSA()
	{
		this(0);
	}
	
	public UnknownSA(int value)
	{
		super("casa", "com.apple.itunes.unknown-sa", value);
	}
}
