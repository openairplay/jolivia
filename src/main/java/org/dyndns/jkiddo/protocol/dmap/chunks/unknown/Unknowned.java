package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.BooleanChunk;

public class Unknowned extends BooleanChunk
{
	public Unknowned()
	{
		this(false);
	}

	public Unknowned(boolean i)
	{
		super("msed", "com.apple.itunes.unknown-ed", i);
	}
}
