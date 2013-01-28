package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

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
