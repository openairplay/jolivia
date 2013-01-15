package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class UnknownHI extends UIntChunk
{
	public UnknownHI()
	{
		this(0);
	}

	public UnknownHI(int value)
	{
		super("mshi", "com.apple.itunes.unknown-hi", value);
	}
}
