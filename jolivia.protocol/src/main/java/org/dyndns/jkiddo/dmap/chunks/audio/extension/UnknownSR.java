package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSR extends BooleanChunk
{
	public UnknownSR()
	{
		this(false);
	}

	public UnknownSR(boolean i)
	{
		super("aeSR", "com.apple.itunes.unknown-SR", i);
	}
}
