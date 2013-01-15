package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

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
