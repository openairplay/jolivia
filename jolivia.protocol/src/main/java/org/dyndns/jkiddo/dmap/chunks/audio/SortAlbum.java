package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class SortAlbum extends StringChunk
{
	public SortAlbum()
	{
		this("");
	}

	public SortAlbum(String b)
	{
		super("assu", "daap.sortalbum", b);
	}
}
