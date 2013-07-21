package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;


public class SongAlbumUserRating extends BooleanChunk
{
	public SongAlbumUserRating()
	{
		this(false);
	}

	public SongAlbumUserRating(boolean b)
	{
		super("asrs", "daap.songalbumuserrating", b);
	}
}