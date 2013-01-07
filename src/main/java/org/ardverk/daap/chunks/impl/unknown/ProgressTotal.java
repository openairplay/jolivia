package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UIntChunk;

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
