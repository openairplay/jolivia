package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.StringChunk;

public class NumberGenre extends StringChunk
{
	public NumberGenre()
	{
		this(null);
	}

	public NumberGenre(String value)
	{
		super("cang", "com.apple.itunes.unknown-ng", value);
	}

}
