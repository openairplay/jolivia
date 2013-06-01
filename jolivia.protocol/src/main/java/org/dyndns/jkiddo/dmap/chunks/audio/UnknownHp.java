package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownHp extends BooleanChunk
{
	public UnknownHp()
	{
		this(false);
	}

	public UnknownHp(boolean value)
	{
		super("ashp", "daap.unknown-hp", value);
	}
}
