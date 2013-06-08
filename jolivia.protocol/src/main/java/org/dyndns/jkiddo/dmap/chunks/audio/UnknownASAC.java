package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownASAC extends BooleanChunk
{
	public UnknownASAC()
	{
		this(false);
	}

	public UnknownASAC(boolean b)
	{
		super("asac", "daap.unknown-ac", b);
	}
}
