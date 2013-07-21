package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class SongHasBeenPlayed extends BooleanChunk
{
	public SongHasBeenPlayed()
	{
		this(false);
	}

	public SongHasBeenPlayed(boolean value)
	{
		super("ashp", "daap.songhasbeenplayed", value);
	}
}
