package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class UnknownAS extends UIntChunk
{
	public UnknownAS()
	{
		this(0);
	}

	public UnknownAS(int value)
	{
		super("caas", "com.apple.itunes.unknown-as", value);
	}

}
