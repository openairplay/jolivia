package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownGU extends BooleanChunk
{
	public UnknownGU()
	{
		this(false);
	}

	public UnknownGU(boolean b)
	{
		super("aeGU", "daap.unknown-GU", b);
	}
}
