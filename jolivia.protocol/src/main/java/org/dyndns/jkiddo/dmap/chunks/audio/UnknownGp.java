package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownGp extends BooleanChunk
{
	public UnknownGp()
	{
		this(false);
	}

	public UnknownGp(boolean value)
	{
		super("asgp", "daap.unknown-gp", value);
	}
}
