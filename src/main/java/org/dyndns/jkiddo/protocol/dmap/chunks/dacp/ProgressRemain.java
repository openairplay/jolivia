package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

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
