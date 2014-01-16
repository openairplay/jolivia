package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class LikedState extends UIntChunk
{
	public LikedState()
	{
		this(0);
	}

	public LikedState(int value)
	{
		super("aels", "com.apple.itunes.liked-state", value);
	}
}