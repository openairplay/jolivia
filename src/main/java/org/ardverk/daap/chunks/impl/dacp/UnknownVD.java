package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UByteChunk;

public class UnknownVD extends UByteChunk
{
	public UnknownVD()
	{
		this(0);
	}

	public UnknownVD(int value)
	{
		super("cavd", "com.apple.itunes.unknown-vd", value);
	}

}
