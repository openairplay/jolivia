package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

public class GaplessResy extends ULongChunk
{
	public GaplessResy()
	{
		this(0);
	}

	public GaplessResy(long value)
	{
		super("aeGR", "com.apple.itunes.gapless-resy", value);
	}
}
