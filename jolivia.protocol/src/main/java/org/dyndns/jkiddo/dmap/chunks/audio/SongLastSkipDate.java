package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.DateChunk;

public class SongLastSkipDate extends DateChunk
{
	public SongLastSkipDate()
	{
		this(0);
	}

	public SongLastSkipDate(int b)
	{
		super("askd", "daap.songlastskipdate", b);
	}
}
