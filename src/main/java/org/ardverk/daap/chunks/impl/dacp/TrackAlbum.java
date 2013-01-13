package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.StringChunk;

public class TrackAlbum extends StringChunk
{
	public TrackAlbum()
	{
		this(null);
	}

	public TrackAlbum(String value)
	{
		super("canl", "com.apple.itunes.unknown-nl", value);
	}

}
