package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.asls)
public class SongLongSize extends ULongChunk
{
	public SongLongSize()
	{
		this(0);
	}

	public SongLongSize(int v)
	{
		super("asls", "daap.songlongsize", v);
	}
}
