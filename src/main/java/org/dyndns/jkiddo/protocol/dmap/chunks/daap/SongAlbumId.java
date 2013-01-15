package org.dyndns.jkiddo.protocol.dmap.chunks.daap;

import org.dyndns.jkiddo.protocol.dmap.chunks.ULongChunk;

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
