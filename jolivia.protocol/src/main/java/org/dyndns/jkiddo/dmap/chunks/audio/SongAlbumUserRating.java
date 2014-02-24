package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;


import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aslr)
public class SongAlbumUserRating extends BooleanChunk
{
	public SongAlbumUserRating()
	{
		this(false);
	}

	public SongAlbumUserRating(boolean b)
	{
		super("aslr", "daap.songalbumuserrating", b);
	}
}