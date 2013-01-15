package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class UnknownMK extends UIntChunk
{
	public UnknownMK()
	{
		this(0);
	}

	public UnknownMK(int value)
	{
		super("cmmk", "com.apple.itunes.unknown-mk", value);
	}

}
