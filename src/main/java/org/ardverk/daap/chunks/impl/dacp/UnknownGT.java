package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.ContainerChunk;

public class UnknownGT extends ContainerChunk
{
	public UnknownGT()
	{
		super("cmgt", "com.apple.itunes.unknown-gt");
	}

	public RelativeVolume getMasterVolume()
	{
		return getSingleChunk(RelativeVolume.class);
	}
}
