package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

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
