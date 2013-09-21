package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

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
