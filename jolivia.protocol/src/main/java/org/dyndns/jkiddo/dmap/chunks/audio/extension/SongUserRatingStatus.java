package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.asrs)
public class SongUserRatingStatus extends BooleanChunk
{
	public SongUserRatingStatus()
	{
		this(false);
	}

	public SongUserRatingStatus(boolean b)
	{
		super("asrs", "daap.songuserratingstatus", b);
	}
}
