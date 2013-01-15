package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UShortChunk;

public class UnknownHC extends UShortChunk
{
	public UnknownHC()
	{
		this(0);
	}

	public UnknownHC(int value)
	{
		super("mshc", "com.apple.itunes.unknown-hl", value);
	}
}
