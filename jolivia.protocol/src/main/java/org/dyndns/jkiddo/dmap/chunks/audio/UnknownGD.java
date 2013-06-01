package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownGD extends BooleanChunk
{
	public UnknownGD()
	{
		this(false);
	}

	public UnknownGD(boolean b)
	{
		super("aeGD", "daap.unknown-GD", b);
	}
}
