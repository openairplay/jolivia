package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownNN extends BooleanChunk
{
	public UnknownNN()
	{
		this(false);
	}

	public UnknownNN(boolean b)
	{
		super("aeNN", "daap.unknown-NN", b);
	}
}
