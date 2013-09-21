package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class SongExcludeFromShuffle extends BooleanChunk
{
	public SongExcludeFromShuffle()
	{
		this(false);
	}

	public SongExcludeFromShuffle(boolean b)
	{
		super("ases", "daap.songexcludefromshuffle", b);
	}
}