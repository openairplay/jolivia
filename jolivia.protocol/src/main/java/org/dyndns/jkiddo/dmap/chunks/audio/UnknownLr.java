package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;


public class UnknownLr extends BooleanChunk
{
	public UnknownLr()
	{
		this(false);
	}

	public UnknownLr(boolean b)
	{
		super("asrs", "daap.unknown-rs", b);
	}
}