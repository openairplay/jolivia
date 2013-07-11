package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownGR extends BooleanChunk
{
	public UnknownGR()
	{
		this(false);
	}

	public UnknownGR(boolean value)
	{
		super("aeGR", "daap.unknown-GR", value);
	}
}
