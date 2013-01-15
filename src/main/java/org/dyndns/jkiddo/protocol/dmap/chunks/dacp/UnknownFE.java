package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class UnknownFE extends UByteChunk
{
	public UnknownFE()
	{
		this(0);
	}

	public UnknownFE(int value)
	{
		super("cafe", "com.apple.itunes.unknown-fe", value);
	}

}
