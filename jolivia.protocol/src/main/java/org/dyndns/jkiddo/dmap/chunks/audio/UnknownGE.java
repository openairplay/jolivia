package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownGE extends BooleanChunk
{
	public UnknownGE()
	{
		this(false);
	}

	public UnknownGE(boolean value)
	{
		super("aeGE", "daap.unknown-GE", value);
	}
}
