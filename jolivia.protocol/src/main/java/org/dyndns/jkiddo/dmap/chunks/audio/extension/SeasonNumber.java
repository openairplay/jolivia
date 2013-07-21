package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class SeasonNumber extends UIntChunk
{
	public SeasonNumber()
	{
		this(0);
	}

	public SeasonNumber(int b)
	{
		super("aeSU", "com.apple.itunes.season-num", b);
	}
}
