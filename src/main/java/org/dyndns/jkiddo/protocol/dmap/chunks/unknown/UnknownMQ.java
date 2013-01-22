package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.BooleanChunk;

public class UnknownMQ extends BooleanChunk
{
	public UnknownMQ()
	{
		this(false);
	}

	public UnknownMQ(boolean i)
	{
		super("aeMQ", "com.apple.itunes.unknown-MQ", i);
	}
}
