package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownES extends BooleanChunk
{
	public UnknownES()
	{
		this(false);
	}

	public UnknownES(boolean b)
	{
		super("aeES", "daap.unknown-ES", b);
	}
}
