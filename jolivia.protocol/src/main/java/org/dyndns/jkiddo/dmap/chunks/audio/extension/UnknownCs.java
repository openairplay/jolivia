package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownCs extends BooleanChunk
{
	public UnknownCs()
	{
		this(false);
	}

	public UnknownCs(boolean b)
	{
		super("aeCS", "com.apple.itunes.artworkchecksum", b);
	}
}
