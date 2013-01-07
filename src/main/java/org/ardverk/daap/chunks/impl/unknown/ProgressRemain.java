package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UIntChunk;

public class ProgressRemain extends UIntChunk
{
	public ProgressRemain()
	{
		this(0);
	}

	public ProgressRemain(int value)
	{
		super("cant", "com.apple.itunes.unknown-nt", value);
	}

}
