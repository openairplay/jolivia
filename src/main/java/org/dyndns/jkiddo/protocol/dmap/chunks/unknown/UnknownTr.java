package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.BooleanChunk;

public class UnknownTr extends BooleanChunk
{
	public UnknownTr()
	{
		this(false);
	}

	public UnknownTr(boolean i)
	{
		super("aeTr", "com.apple.itunes.unknown-Tr", i);
	}
}
