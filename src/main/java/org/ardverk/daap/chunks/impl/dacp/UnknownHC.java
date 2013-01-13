package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UShortChunk;

public class UnknownHC extends UShortChunk
{
	public UnknownHC()
	{
		this(0);
	}

	public UnknownHC(int value)
	{
		super("mshc", "com.apple.itunes.unknown-hl", value);
	}
}
