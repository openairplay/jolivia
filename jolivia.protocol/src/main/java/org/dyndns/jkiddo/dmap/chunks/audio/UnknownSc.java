package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSc extends BooleanChunk
{
	public UnknownSc()
	{
		this(false);
	}

	public UnknownSc(boolean b)
	{
		super("assc", "daap.unknown-sc", b);
	}
}
