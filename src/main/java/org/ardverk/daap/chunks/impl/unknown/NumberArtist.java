package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.StringChunk;

public class NumberArtist extends StringChunk
{
	public NumberArtist()
	{
		this(null);
	}
	
	public NumberArtist(String value)
	{
		super("cana", "com.apple.itunes.unknown-na", value);
	}

}
