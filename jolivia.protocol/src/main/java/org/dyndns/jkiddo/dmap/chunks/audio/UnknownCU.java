package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

public class UnknownCU extends ULongChunk
{
	public UnknownCU()
	{
		this(0);

	}

	public UnknownCU(int b)
	{
		super("mscu", "unknown-cu", b);
	}
}
