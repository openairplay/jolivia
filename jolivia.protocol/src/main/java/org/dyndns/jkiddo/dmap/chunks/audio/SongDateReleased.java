package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.DateChunk;

public class SongDateReleased extends DateChunk
{
	public SongDateReleased()
	{
		this(0l);
	}

	public SongDateReleased(long l)
	{
		super("asdr", "daap.songdatereleased", l);
	}
}
