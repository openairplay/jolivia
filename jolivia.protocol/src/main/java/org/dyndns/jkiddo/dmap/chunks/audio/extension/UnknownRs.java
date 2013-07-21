package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownRs extends BooleanChunk
{
	public UnknownRs()
	{
		this(false);
	}

	public UnknownRs(boolean b)
	{
		super("asrs", "daap.songuserratingstatus", b);
	}
}
