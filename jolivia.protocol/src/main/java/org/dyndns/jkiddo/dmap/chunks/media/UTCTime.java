package org.dyndns.jkiddo.dmap.chunks.media;

import org.dyndns.jkiddo.dmap.chunks.DateChunk;

public class UTCTime extends DateChunk
{
	public UTCTime()
	{
		this(0);
	}

	public UTCTime(long i)
	{
		super("mstc", "dmap.utctime", i);
	}
}
