package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class Voting extends BooleanChunk
{
	public Voting()
	{
		this(false);
	}

	public Voting(boolean b)
	{
		super("ceVO", "com.apple.itunes.unknown-voting", b);
	}
}
