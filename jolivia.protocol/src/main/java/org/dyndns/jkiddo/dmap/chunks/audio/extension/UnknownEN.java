package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownEN extends BooleanChunk
{
	public UnknownEN()
	{
		this(false);
	}

	public UnknownEN(boolean b)
	{
		super("aeEN", "daap.unknown-EN", b);
	}
}
