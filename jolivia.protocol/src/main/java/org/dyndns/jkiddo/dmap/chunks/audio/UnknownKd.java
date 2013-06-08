package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownKd extends BooleanChunk
{
	public UnknownKd()
	{
		this(false);
	}

	public UnknownKd(boolean b)
	{
		super("askd", "daap.unknown-kd", b);
	}
}
