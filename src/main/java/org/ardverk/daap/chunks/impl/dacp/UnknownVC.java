package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.UByteChunk;

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
