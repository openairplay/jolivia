package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class SongUserRatingStatus extends BooleanChunk
{
	public SongUserRatingStatus()
	{
		this(false);
	}

	public SongUserRatingStatus(boolean b)
	{
		super("asrs", "daap.songuserratingstatus", b);
	}
}
