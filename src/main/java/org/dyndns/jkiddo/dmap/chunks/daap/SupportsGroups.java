package org.dyndns.jkiddo.dmap.chunks.daap;

import org.dyndns.jkiddo.dmap.chunks.UShortChunk;

public class SupportsGroups extends UShortChunk
{
	public SupportsGroups()
	{
		this(0);
	}

	public SupportsGroups(int i)
	{
		super("asgr", "daap.supportsgroups", i);
	}
}
