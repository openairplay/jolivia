package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class UnknownVE extends UByteChunk
{
	public UnknownVE()
	{
		this(0);
	}

	public UnknownVE(int value)
	{
		super("cave", "com.apple.itunes.unknown-ve", value);
	}
}
