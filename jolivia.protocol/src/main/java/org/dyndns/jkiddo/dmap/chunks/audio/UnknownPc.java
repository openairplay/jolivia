package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownPc extends BooleanChunk
{
	public UnknownPc()
	{
		this(false);
	}

	public UnknownPc(boolean b)
	{
		super("aspc", "daap.unknown-pc", b);
	}
}
