package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UByteChunk;

public class VisualizerStatus extends UByteChunk
{
	public VisualizerStatus()
	{
		this(0);
	}

	public VisualizerStatus(int i)
	{
		super("cavs", "com.apple.itunes.unknown-vs", i);
	}

}
