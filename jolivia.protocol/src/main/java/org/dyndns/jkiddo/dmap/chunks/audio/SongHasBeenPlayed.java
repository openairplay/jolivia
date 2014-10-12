package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ashp)
public class SongHasBeenPlayed extends BooleanChunk
{
	public SongHasBeenPlayed()
	{
		this(false);
	}

	public SongHasBeenPlayed(boolean value)
	{
		super("ashp", "daap.songhasbeenplayed", value);
	}
}
