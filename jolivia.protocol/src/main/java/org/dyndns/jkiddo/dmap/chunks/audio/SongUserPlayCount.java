package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aspc)
public class SongUserPlayCount extends UIntChunk
{
	public SongUserPlayCount()
	{
		this(0);
	}

	public SongUserPlayCount(int b)
	{
		super("aspc", "daap.songuserplaycount", b);
	}
}
