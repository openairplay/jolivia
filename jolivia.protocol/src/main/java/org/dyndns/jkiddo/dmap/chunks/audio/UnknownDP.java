package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownDP extends BooleanChunk
{
	public UnknownDP()
	{
		this(false);
	}

	public UnknownDP(boolean value)
	{
		super("aeDP", "daap.unknown-dp", value);
	}
}
