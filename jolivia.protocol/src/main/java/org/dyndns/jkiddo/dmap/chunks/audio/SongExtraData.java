package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.UShortChunk;

public class SongExtraData extends UShortChunk
{
	// 1 = album art
	public SongExtraData()
	{
		this(0);
	}

	public SongExtraData(int value)
	{
		super("ased", "daap.songextradata", value);
	}
}
