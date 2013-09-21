package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.DateChunk;

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
