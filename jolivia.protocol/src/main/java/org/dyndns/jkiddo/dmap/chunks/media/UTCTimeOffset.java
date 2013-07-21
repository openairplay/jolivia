package org.dyndns.jkiddo.dmap.chunks.media;

import org.dyndns.jkiddo.dmap.chunks.SIntChunk;

public class UTCTimeOffset extends SIntChunk
{
	public UTCTimeOffset()
	{
		this(0);
	}

	public UTCTimeOffset(int i)
	{
		super("msto", "dmap.utcoffset", i);
	}
}
