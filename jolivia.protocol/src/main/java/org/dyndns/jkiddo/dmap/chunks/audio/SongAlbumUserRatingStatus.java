package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class SongAlbumUserRatingStatus extends BooleanChunk
{
	public SongAlbumUserRatingStatus()
	{
		this(false);
	}

	public SongAlbumUserRatingStatus(boolean b)
	{
		super("asas", "daap.songalbumuserratingstatus", b);
	}
}