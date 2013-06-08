package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownAs extends BooleanChunk
{
	public UnknownAs()
	{
		this(false);
	}

	public UnknownAs(boolean b)
	{
		super("asas", "daap.unknown-as", b);
	}
}