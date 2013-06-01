package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownASSU extends BooleanChunk
{
	public UnknownASSU()
	{
		this(false);
	}

	public UnknownASSU(boolean b)
	{
		super("assu", "daap.unknown-su", b);
	}
}
