package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class SongGapless extends BooleanChunk
{
	public SongGapless()
	{
		this(false);
	}

	public SongGapless(boolean value)
	{
		super("asgp", "daap.songgapless", value);
	}
}
