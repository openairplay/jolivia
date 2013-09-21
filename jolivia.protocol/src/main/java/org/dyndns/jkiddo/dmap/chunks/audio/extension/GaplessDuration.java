package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

public class GaplessDuration extends ULongChunk
{
	public GaplessDuration()
	{
		this(0);
	}

	public GaplessDuration(int b)
	{
		super("aeGU", "com.apple.itunes.gapless-dur", b);
	}
}
