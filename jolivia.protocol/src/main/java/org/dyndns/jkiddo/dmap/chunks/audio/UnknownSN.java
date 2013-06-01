package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSN extends BooleanChunk
{
	public UnknownSN()
	{
		this(false);
	}

	public UnknownSN(boolean b)
	{
		super("aeSN", "daap.unknown-SN", b);
	}
}
