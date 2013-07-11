package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownDV extends BooleanChunk
{
	public UnknownDV()
	{
		this(false);
	}

	public UnknownDV(boolean b)
	{
		super("aeDV", "daap.unknown-DV", b);
	}
}
