package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UShortChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.asac)
public class SongArtworkCount extends UShortChunk
{
	public SongArtworkCount()
	{
		this(0);
	}

	public SongArtworkCount(int b)
	{
		super("asac", "daap.songartworkcount", b);
	}
}
