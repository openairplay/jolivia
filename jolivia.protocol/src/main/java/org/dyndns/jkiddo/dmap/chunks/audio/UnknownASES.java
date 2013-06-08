package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownASES extends BooleanChunk
{
	public UnknownASES()
	{
		this(false);
	}

	public UnknownASES(boolean b)
	{
		super("ases", "daap.unknown-es", b);
	}
}