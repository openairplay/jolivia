package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSa extends BooleanChunk
{
	public UnknownSa()
	{
		this(false);
	}

	public UnknownSa(boolean value)
	{
		super("assa", "daap.unknown-sa", value);
	}
}
