package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.StringChunk;

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
