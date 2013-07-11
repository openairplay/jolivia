package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownMX extends BooleanChunk
{
	public UnknownMX()
	{
		this(false);
	}

	public UnknownMX(boolean b)
	{
		super("aeMX", "daap.unknown-MX", b);
	}
}
