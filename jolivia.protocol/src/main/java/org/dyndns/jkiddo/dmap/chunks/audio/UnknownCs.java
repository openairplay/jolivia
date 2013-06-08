package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownCs extends BooleanChunk
{
	public UnknownCs()
	{
		this(false);
	}

	public UnknownCs(boolean b)
	{
		super("aeCS", "daap.unknown-CS", b);
	}
}
