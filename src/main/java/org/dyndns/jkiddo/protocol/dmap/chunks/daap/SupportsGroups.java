package org.dyndns.jkiddo.protocol.dmap.chunks.daap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UShortChunk;

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
