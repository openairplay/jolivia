package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class SortAlbumArtist extends StringChunk
{
	public SortAlbumArtist()
	{
		this("");
	}

	public SortAlbumArtist(String value)
	{
		super("assl", "daap.sortalbumartist", value);
	}
}
