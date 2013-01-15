package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.ULongChunk;

public class UnknownIM extends ULongChunk
{
	public UnknownIM()
	{
		this(0);
	}

	public UnknownIM(long value)
	{
		super("aeIM", "com.apple.itunes.unknown-IM", value);
	}
}
