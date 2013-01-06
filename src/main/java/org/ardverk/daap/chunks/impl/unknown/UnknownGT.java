package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.ContainerChunk;
import org.ardverk.daap.chunks.impl.RelativeVolume;

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
