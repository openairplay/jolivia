package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.UIntChunk;

public class MediaKind extends UIntChunk
{
	public MediaKind()
	{
		this(0);
	}

	public MediaKind(int value)
	{
		super("aeMk", "com.apple.itunes.extended-media-kind", value);
	}
}
