package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.UIntChunk;

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
