package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownND extends BooleanChunk
{
	public UnknownND()
	{
		this(false);
	}

	public UnknownND(boolean b)
	{
		super("aeND", "daap.unknown-ND", b);
	}
}
