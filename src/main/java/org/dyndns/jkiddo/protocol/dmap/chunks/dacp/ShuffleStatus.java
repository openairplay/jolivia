package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class ShuffleStatus extends UByteChunk
{
	public ShuffleStatus()
	{
		this(0);
	}

	public ShuffleStatus(int value)
	{
		super("cash", "com.apple.itunes.unknown-sh", value);
	}

}
