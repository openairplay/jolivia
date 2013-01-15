package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class RepeatStatus extends UByteChunk
{
	public RepeatStatus()
	{
		this(0);
	}

	public RepeatStatus(int value)
	{
		super("carp", "com.apple.itunes.unknown-rp", value);
	}

}
