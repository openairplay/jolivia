package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class UnknownSU extends UByteChunk
{
	public UnknownSU()
	{
		this(0);
	}

	public UnknownSU(int value)
	{
		super("casu", "com.apple.itunes.unknown-su", value);
	}
}
