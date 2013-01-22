package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class Unknownto extends UIntChunk
{
	public Unknownto()
	{
		this(0);
	}

	public Unknownto(int i)
	{
		super("msto", "com.apple.itunes.unknown-to", i);
	}
}
