package org.ardverk.daap.chunks.impl.dmap;

import org.ardverk.daap.chunks.UByteChunk;

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
