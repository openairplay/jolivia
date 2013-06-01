package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownLs extends BooleanChunk
{
	public UnknownLs()
	{
		this(false);
	}

	public UnknownLs(boolean v)
	{
		super("asls", "daap.unknown-asls", v);
	}
}
