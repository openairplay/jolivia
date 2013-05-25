package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.UByteChunk;

public class UnknownFP extends UByteChunk
{
	public UnknownFP()
	{
		this(0);
	}

	public UnknownFP(int i)
	{
		super("aeFP", "com.apple.itunes.unknown-FP", i);
	}
}
