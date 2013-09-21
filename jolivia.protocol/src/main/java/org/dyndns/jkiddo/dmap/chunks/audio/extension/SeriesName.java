package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class SeriesName extends StringChunk
{
	public SeriesName()
	{
		this("");
	}

	public SeriesName(String s)
	{
		super("aeSN", "com.apple.itunes.series-name", s);
	}
}
