package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

public class TrackArtist extends StringChunk
{
	public TrackArtist()
	{
		this(null);
	}
	
	public TrackArtist(String value)
	{
		super("cana", "com.apple.itunes.unknown-na", value);
	}

}
