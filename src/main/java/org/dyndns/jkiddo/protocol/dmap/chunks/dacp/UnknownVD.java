package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class UnknownVD extends UByteChunk
{
	public UnknownVD()
	{
		this(0);
	}

	public UnknownVD(int value)
	{
		super("cavd", "com.apple.itunes.unknown-vd", value);
	}

}
