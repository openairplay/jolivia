package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSU extends BooleanChunk
{
	public UnknownSU()
	{
		this(false);
	}

	public UnknownSU(boolean b)
	{
		super("aeSU", "daap.unknown-SU", b);
	}
}
