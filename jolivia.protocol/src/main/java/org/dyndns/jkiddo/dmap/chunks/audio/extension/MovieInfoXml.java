package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class MovieInfoXml extends StringChunk
{
	public MovieInfoXml()
	{
		this("");
	}

	public MovieInfoXml(String b)
	{
		super("aeMX", "com.apple.itunes.movie-info-xml", b);
	}
}
