package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UIntChunk;

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
