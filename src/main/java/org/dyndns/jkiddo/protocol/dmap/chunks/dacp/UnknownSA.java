package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class UnknownSA extends UIntChunk
{
	public UnknownSA()
	{
		this(0);
	}
	
	public UnknownSA(int value)
	{
		super("casa", "com.apple.itunes.unknown-sa", value);
	}
}
