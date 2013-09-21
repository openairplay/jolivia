package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class SongUserSkipCount extends UIntChunk
{
	public SongUserSkipCount()
	{
		this(0);
	}

	public SongUserSkipCount(int b)
	{
		super("askp", "daap.songuserskipcount", b);
	}
}
