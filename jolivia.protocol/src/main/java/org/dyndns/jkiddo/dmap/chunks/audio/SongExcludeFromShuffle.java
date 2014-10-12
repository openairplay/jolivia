package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ases)
public class SongExcludeFromShuffle extends BooleanChunk
{
	public SongExcludeFromShuffle()
	{
		this(false);
	}

	public SongExcludeFromShuffle(boolean b)
	{
		super("ases", "daap.songexcludefromshuffle", b);
	}
}