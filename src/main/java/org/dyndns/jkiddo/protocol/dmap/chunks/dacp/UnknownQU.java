package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class UnknownQU extends UByteChunk
{
	public UnknownQU()
	{
		this(0);
	}

	public UnknownQU(int value)
	{
		super("ceQu", "com.apple.itunes.unknown-Qu", value);
	}

}
