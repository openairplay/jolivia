package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.ULongChunk;

public class UnknownSX extends ULongChunk
{
	public UnknownSX()
	{
		this(0);
	}

	public UnknownSX(int i)
	{
		super("aeSX", "com.apple.itunes.unknown-SX", i);
	}
}
