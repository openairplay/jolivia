package org.ardverk.daap.chunks.impl.daap;

import org.ardverk.daap.chunks.ULongChunk;

public class SongAlbumId extends ULongChunk
{
	public SongAlbumId()
	{
		this(0);
	}

	public SongAlbumId(long value)
	{
		super("asai", "daap.songalbumid", value);
	}

}
