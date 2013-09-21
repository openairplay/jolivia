package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.DateChunk;

public class SongDatePlayed extends DateChunk
{
	public SongDatePlayed()
	{
		this(0);
	}

	public SongDatePlayed(int b)
	{
		super("aspl", "daap.songdateplayed", b);
	}
}
