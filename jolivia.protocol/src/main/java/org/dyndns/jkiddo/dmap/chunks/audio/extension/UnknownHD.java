package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class UnknownHD extends BooleanChunk
{
	public UnknownHD()
	{
		this(false);
	}

	public UnknownHD(boolean b)
	{
		super("aeHD","com.apple.itunes.unknown-HD",b);
	}
}
