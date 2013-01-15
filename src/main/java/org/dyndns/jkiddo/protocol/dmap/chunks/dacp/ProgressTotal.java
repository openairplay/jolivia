package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class ProgressTotal extends UIntChunk
{
	public ProgressTotal()
	{
		this(0);
	}

	public ProgressTotal(int value)
	{
		super("cast", "com.apple.itunes.unknown-st", value);
	}

}
