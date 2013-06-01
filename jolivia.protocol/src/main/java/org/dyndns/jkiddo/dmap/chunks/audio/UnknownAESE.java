package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownAESE extends BooleanChunk
{
	public UnknownAESE()
	{
		this(false);
	}

	public UnknownAESE(boolean b)
	{
		super("aeSE", "daap.unknown-SE", b);
	}
}
