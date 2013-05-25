package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.ULongChunk;

public class UnknownSE extends ULongChunk
{
	public UnknownSE()
	{
		this(0);
	}

	public UnknownSE(int i)
	{
		super("asse", "com.apple.itunes.unknown-se", i);
	}
}
