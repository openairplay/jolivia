package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class PlayStatus extends UByteChunk
{
	public PlayStatus()
	{
		this(0);
	}

	public PlayStatus(int value)
	{
		super("caps", "com.apple.itunes.unknown-ps", value);
	}

}
