package org.ardverk.daap.chunks.impl.com.apple.itunes;

import org.ardverk.daap.chunks.UIntChunk;

public class EMediaKind extends UIntChunk
{
	public EMediaKind()
	{
		this(0);
	}

	public EMediaKind(int value)
	{
		super("aeMk", "com.apple.itunes.extended-media-kind", value);
	}
}
