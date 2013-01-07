package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UIntChunk;

public class UnknownMK extends UIntChunk
{
	public UnknownMK()
	{
		this(0);
	}

	public UnknownMK(int value)
	{
		super("cmmk", "com.apple.itunes.unknown-mk", value);
		// TODO Auto-generated constructor stub
	}

}
