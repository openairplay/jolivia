package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

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
