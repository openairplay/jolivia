package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.RawChunk;

public class UnknownHN extends RawChunk
{
	public UnknownHN()
	{
		this(new byte[]{});
	}

	public UnknownHN(byte[] value)
	{
		super("mshn", "com.apple.itunes.unknown-hn", value);
	}
}
