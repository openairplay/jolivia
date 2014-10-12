package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.asgp)
public class SongGapless extends BooleanChunk
{
	public SongGapless()
	{
		this(false);
	}

	public SongGapless(boolean value)
	{
		super("asgp", "daap.songgapless", value);
	}
}
