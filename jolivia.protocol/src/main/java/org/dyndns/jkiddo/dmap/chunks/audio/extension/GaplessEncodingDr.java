package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class GaplessEncodingDr extends UIntChunk
{
	public GaplessEncodingDr()
	{
		this(0);
	}

	public GaplessEncodingDr(int b)
	{
		super("aeGD", "com.apple.itunes.gapless-enc-dr", b);
	}
}
