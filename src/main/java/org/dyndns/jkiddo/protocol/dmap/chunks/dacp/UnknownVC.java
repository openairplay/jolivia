package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class UnknownVC extends UByteChunk
{
	public UnknownVC()
	{
		this(0);
	}

	public UnknownVC(int value)
	{
		super("cavc", "com.apple.itunes.unknown-vc", value);
	}

}
