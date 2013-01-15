package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class RelativeVolume extends UIntChunk
{
	public RelativeVolume()
	{
		this(0);
	}

	public RelativeVolume(long value)
	{
		super("cmvo", "com.apple.itunes.unknown-vo", value);
	}

}
