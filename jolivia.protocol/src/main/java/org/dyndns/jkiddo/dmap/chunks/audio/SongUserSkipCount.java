package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.askp)
public class SongUserSkipCount extends UIntChunk
{
	public SongUserSkipCount()
	{
		this(0);
	}

	public SongUserSkipCount(int b)
	{
		super("askp", "daap.songuserskipcount", b);
	}
}
