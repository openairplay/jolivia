package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

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
