package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownKp extends BooleanChunk
{
	public UnknownKp()
	{
		this(false);
	}

	public UnknownKp(boolean b)
	{
		super("askp", "daap.unknown-kp", b);
	}
}
