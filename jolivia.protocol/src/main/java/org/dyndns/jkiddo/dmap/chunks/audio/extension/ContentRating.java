package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class ContentRating extends StringChunk
{
	public ContentRating()
	{
		this("");
	}

	public ContentRating(String b)
	{
		super("aeCR", "com.apple.itunes.content-rating", b);
	}
}
