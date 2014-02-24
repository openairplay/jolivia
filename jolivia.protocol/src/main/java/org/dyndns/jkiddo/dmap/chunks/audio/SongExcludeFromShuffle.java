package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ases)
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