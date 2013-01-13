package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.StringChunk;

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
