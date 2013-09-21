package org.dyndns.jkiddo.dmp.chunks.unknown;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

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
