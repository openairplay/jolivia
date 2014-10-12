package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.DateChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aspl)
public class SongDatePlayed extends DateChunk
{
	public SongDatePlayed()
	{
		this(0);
	}

	public SongDatePlayed(int b)
	{
		super("aspl", "daap.songdateplayed", b);
	}
}
