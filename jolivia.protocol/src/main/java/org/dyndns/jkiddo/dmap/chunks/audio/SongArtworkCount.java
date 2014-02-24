package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UShortChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.asac)
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
