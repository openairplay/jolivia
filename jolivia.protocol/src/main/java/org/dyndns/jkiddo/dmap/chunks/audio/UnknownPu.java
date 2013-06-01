package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownPu extends BooleanChunk
{
	public UnknownPu()
	{
		this(false);
	}

	public UnknownPu(boolean v)
	{
		super("aspu", "daap.unknown-pu", v);
	}
}
