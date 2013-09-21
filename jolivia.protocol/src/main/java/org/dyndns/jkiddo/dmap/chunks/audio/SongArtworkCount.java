package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UShortChunk;

public class SongArtworkCount extends UShortChunk
{
	public SongArtworkCount()
	{
		this(0);
	}

	public SongArtworkCount(int b)
	{
		super("asac", "daap.songartworkcount", b);
	}
}
