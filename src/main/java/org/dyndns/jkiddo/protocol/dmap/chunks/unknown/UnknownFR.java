package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class UnknownFR extends UByteChunk
{
	public UnknownFR()
	{
		this(0);
	}

	public UnknownFR(int i)
	{
		super("aeFR", "com.apple.itunes.unknown-FR", i);
	}
}
