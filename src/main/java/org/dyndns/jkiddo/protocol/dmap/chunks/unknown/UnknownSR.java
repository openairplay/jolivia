package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.BooleanChunk;

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
