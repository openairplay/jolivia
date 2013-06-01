package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownBK extends BooleanChunk
{
	public UnknownBK()
	{
		this(false);
	}

	public UnknownBK(boolean b)
	{
		super("asbk", "daap.unknown-bk", b);
	}
}
