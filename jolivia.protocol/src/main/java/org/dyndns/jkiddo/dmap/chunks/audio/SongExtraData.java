package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UShortChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ased)
public class SongExtraData extends UShortChunk
{
	// 1 = album art
	public SongExtraData()
	{
		this(0);
	}

	public SongExtraData(int value)
	{
		super("ased", "daap.songextradata", value);
	}
}
