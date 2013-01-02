package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.UIntChunk;

public class ArtworkChecksum extends UIntChunk
{

	public ArtworkChecksum()
	{
		this(0);
	}

	public ArtworkChecksum(int value)
	{
		super("aeCs", "com.apple.itunes.artworkchecksum", value);
	}
}
