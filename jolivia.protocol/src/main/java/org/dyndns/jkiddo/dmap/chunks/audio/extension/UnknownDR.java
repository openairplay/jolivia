package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownDR extends BooleanChunk
{
	public UnknownDR()
	{
		this(false);
	}

	public UnknownDR(boolean b)
	{
		super("aeDR", "daap.unknown-DR", b);
	}
}
