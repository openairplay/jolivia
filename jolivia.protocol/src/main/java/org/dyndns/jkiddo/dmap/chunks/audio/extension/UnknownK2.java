package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownK2 extends BooleanChunk
{
	public UnknownK2()
	{
		this(false);
	}

	public UnknownK2(boolean b)
	{
		super("aeK2", "daap.unknown-K2", b);
	}
}
