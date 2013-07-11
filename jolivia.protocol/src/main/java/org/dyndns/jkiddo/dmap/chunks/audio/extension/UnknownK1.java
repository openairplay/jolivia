package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownK1 extends BooleanChunk
{
	public UnknownK1()
	{
		this(false);
	}

	public UnknownK1(boolean b)
	{
		super("aeK1", "daap.unknown-K1", b);
	}
}
