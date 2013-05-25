package org.dyndns.jkiddo.dmap.chunks.media;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class Unknowntc extends UIntChunk
{
	public Unknowntc()
	{
		this(0);
	}

	public Unknowntc(int i)
	{
		super("mstc", "com.apple.itunes.unknown-tc", i);
	}
}
