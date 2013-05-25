package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

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
