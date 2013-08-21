package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class GaplessResy extends StringChunk
{
	public GaplessResy()
	{
		this("");
	}

	public GaplessResy(String value)
	{
		super("aeGR", "com.apple.itunes.gapless-resy", value);
	}
}
