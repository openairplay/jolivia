package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class UnknownAR extends UIntChunk
{
	public UnknownAR()
	{
		this(0);
	}

	public UnknownAR(int value)
	{
		super("caar", "com.apple.itunes.unknown-ar", value);
	}

}
