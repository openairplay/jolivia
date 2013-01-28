package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSR extends BooleanChunk
{
	public UnknownSR()
	{
		this(false);
	}

	public UnknownSR(boolean i)
	{
		super("aeSR", "com.apple.itunes.unknown-SR", i);
	}
}
