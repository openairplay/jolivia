package org.dyndns.jkiddo.dmap.chunks.media;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class Unknownto extends UIntChunk
{
	public Unknownto()
	{
		this(0);
	}

	public Unknownto(int i)
	{
		super("msto", "com.apple.itunes.unknown-to", i);
	}
}
