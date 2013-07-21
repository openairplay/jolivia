package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.UByteChunk;
import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class ReqFplay extends UIntChunk
{
	public ReqFplay()
	{
		this(0);
	}

	public ReqFplay(int i)
	{
		super("????", "com.apple.itunes.req-fplay", i);
	}
}
