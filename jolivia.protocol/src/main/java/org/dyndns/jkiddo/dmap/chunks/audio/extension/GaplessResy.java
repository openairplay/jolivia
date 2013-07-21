package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class GaplessResy extends BooleanChunk
{
	public GaplessResy()
	{
		this(false);
	}

	public GaplessResy(boolean value)
	{
		super("aeGR", "com.apple.itunes.gapless-resy", value);
	}
}
