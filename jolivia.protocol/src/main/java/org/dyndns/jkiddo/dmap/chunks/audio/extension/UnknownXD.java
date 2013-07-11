package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownXD extends BooleanChunk
{
	public UnknownXD()
	{
		this(false);
	}

	public UnknownXD(boolean b)
	{
		super("aeXD", "daap.unknown-XD", b);
	}
}
