package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.RawChunk;

public class UnknownHN extends RawChunk
{
	public UnknownHN()
	{
		this(new byte[]{});
	}

	public UnknownHN(byte[] value)
	{
		super("mshn", "com.apple.itunes.unknown-hn", value);
	}
}
