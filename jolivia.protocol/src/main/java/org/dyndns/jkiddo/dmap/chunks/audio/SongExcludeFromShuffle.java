package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

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