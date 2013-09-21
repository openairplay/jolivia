package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class SortArtist extends StringChunk
{
	public SortArtist()
	{
		this("");
	}

	public SortArtist(String value)
	{
		super("assa", "daap.sortartist", value);
	}
}
