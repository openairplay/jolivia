package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeES)
public class EpisodeSort extends UIntChunk
{
	public EpisodeSort()
	{
		this(0);
	}

	public EpisodeSort(int b)
	{
		super("aeES", "com.apple.itunes.episode-sort", b);
	}
}
