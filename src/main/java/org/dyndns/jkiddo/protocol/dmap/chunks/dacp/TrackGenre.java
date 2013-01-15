package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

public class TrackGenre extends StringChunk
{
	public TrackGenre()
	{
		this(null);
	}

	public TrackGenre(String value)
	{
		super("cang", "com.apple.itunes.unknown-ng", value);
	}

}
