package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UByteChunk;

public class FullscreenStatus extends UByteChunk
{
	public FullscreenStatus()
	{
		this(0);
	}

	public FullscreenStatus(int i)
	{
		super("cafs", "com.apple.itunes.unknown-fs", i);
	}
}
