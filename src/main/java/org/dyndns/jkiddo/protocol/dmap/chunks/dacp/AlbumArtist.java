package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

public class AlbumArtist extends StringChunk
{
	public AlbumArtist()
	{
		this("");
	}

	public AlbumArtist(String value)
	{
		super("asaa", "com.apple.itunes.unknown-aa", value);
	}
}
