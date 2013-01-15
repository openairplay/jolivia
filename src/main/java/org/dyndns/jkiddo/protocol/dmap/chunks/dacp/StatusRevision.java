package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class StatusRevision extends UIntChunk
{
	public StatusRevision()
	{
		this(0);
	}

	public StatusRevision(int value)
	{
		super("cmsr", "com.apple.itunes.unknown-sr", value);
	}
}
