package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

public class TrackName extends StringChunk
{
	public TrackName()
	{
		this(null);
	}

	public TrackName(String value)
	{
		super("cann", "com.apple.itunes.unknown-nn", value);
	}

}
