package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.BooleanChunk;

public class UnknownSL extends BooleanChunk
{
	public UnknownSL()
	{
		this(false);
	}

	public UnknownSL(boolean i)
	{
		super("aeSL", "com.apple.itunes.unknown-SL", i);
	}
}
