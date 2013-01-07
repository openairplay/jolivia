package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UByteChunk;

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
