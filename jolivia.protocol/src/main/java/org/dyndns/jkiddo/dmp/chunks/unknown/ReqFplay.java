package org.dyndns.jkiddo.dmp.chunks.unknown;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

public class ReqFplay extends UByteChunk
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
