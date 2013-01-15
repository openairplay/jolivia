package org.dyndns.jkiddo.protocol.dmap.chunks.dmap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class GeniusSeed extends UByteChunk
{
	public GeniusSeed()
	{
		this(0);
	}

	public GeniusSeed(int value)
	{
		super("aeGs", "com.apple.itunes.can-be-genius-seed", value);
	}
}
