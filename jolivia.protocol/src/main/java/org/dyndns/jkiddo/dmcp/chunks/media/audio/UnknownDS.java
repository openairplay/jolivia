package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class UnknownDS extends UIntChunk
{
	public UnknownDS()
	{
		this(0);
	}
	
	public UnknownDS(int b)
	{
		super("cads","unknown-ds", b);
	}
}
