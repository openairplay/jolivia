package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.StringChunk;

public class NumberName extends StringChunk
{
	public NumberName()
	{
		this(null);
	}

	public NumberName(String value)
	{
		super("cann", "com.apple.itunes.unknown-nn", value);
	}

}
