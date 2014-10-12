package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.SIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.msto)
public class UTCTimeOffset extends SIntChunk
{
	public UTCTimeOffset()
	{
		this(0);
	}

	public UTCTimeOffset(int i)
	{
		super("msto", "dmap.utcoffset", i);
	}
}
