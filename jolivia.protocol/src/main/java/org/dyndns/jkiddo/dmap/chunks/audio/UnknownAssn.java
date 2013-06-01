package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownAssn extends BooleanChunk
{
	public UnknownAssn()
	{
		this(false);
	}

	public UnknownAssn(boolean value)
	{
		super("assn", "daap.unknown-sn", value);
	}
}
