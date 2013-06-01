package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownGH extends BooleanChunk
{
	public UnknownGH()
	{
		this(false);
	}

	public UnknownGH(boolean b)
	{
		super("aeGH", "daap.unknown-GH", b);
	}
}
