package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.ULongChunk;

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
