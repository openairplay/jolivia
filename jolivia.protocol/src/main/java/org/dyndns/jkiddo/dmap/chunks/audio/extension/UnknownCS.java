package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.UByteChunk;

public class UnknownCS extends UByteChunk{

	public UnknownCS()
	{
		this(0);
	}

	public UnknownCS(int i)
	{
		super("aeCS", "com.apple.itunes.unknown-CS", i);
	}
}
