package org.dyndns.jkiddo.dmp.chunks.unknown;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class ReqFplay extends UIntChunk
{
	public ReqFplay()
	{
		this(0);
	}

	public ReqFplay(int i)
	{
		super("????", "com.apple.itunes.unknown.req-fplay", i);
	}
}
