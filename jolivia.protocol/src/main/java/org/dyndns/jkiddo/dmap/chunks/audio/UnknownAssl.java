package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownAssl extends BooleanChunk
{
	public UnknownAssl()
	{
		this(false);
	}

	public UnknownAssl(boolean value)
	{
		super("assl", "daap.unknown-sl", value);
	}
}
