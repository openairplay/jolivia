package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownEd extends BooleanChunk
{
	public UnknownEd()
	{
		this(false);
	}

	public UnknownEd(boolean value)
	{
		super("ased", "daap.unknown-ed", value);
	}
}
