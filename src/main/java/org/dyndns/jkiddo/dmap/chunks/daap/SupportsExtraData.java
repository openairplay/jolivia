package org.dyndns.jkiddo.dmap.chunks.daap;

import org.dyndns.jkiddo.dmap.chunks.UShortChunk;

public class SupportsExtraData extends UShortChunk
{
	public SupportsExtraData()
	{
		this(0);
	}

	public SupportsExtraData(int i)
	{
		super("ated", "daap.supportsextradata", i);
	}
}
