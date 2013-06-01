package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSS extends BooleanChunk
{
	public UnknownSS()
	{
		this(false);
	}

	public UnknownSS(boolean b)
	{
		super("asss", "daap.unknown-ss", b);
	}
}
