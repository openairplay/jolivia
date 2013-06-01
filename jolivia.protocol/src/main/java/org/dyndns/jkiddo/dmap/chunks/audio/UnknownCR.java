package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownCR extends BooleanChunk
{
	public UnknownCR()
	{
		this(false);
	}

	public UnknownCR(boolean b)
	{
		super("aeCR", "daap.unknown-CR", b);
	}
}
