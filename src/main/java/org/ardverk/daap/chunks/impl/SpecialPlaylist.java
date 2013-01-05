package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.UByteChunk;

public class SpecialPlaylist extends UByteChunk
{
	public SpecialPlaylist()
	{
		this(0);
	}

	public SpecialPlaylist(int mode)
	{
		super("aePS", "com.apple.itunes.special-playlist", mode);
	}
}
