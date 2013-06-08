package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownPl extends BooleanChunk
{
	public UnknownPl()
	{
		this(false);
	}

	public UnknownPl(boolean b)
	{
		super("aspl", "daap.unknown-pl", b);
	}
}
