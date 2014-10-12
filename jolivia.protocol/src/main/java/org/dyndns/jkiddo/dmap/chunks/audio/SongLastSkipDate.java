package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.DateChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.askd)
public class SongLastSkipDate extends DateChunk
{
	public SongLastSkipDate()
	{
		this(0);
	}

	public SongLastSkipDate(int b)
	{
		super("askd", "daap.songlastskipdate", b);
	}
}
