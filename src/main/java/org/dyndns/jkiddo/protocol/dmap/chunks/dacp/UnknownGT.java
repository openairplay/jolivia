package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.ContainerChunk;

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
