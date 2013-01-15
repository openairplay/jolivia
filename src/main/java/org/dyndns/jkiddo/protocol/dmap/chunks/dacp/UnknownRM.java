package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.ULongChunk;

public class UnknownRM extends ULongChunk
{

	public UnknownRM()
	{
		this(0);
	}

	public UnknownRM(long value)
	{
		super("aeRM", "com.apple.itunes.unknown-RM", value);
	}
}
