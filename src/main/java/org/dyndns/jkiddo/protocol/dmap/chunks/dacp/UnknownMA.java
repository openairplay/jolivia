package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.ULongChunk;

public class UnknownMA extends ULongChunk
{
	public UnknownMA()
	{
		this(0);
	}

	public UnknownMA(long value)
	{
		super("msma", "com.apple.itunes.unknown-ma", value);
	}

}
