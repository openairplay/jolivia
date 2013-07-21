package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class SongUserPlayCount extends UIntChunk
{
	public SongUserPlayCount()
	{
		this(0);
	}

	public SongUserPlayCount(int b)
	{
		super("aspc", "daap.songuserplaycount", b);
	}
}
