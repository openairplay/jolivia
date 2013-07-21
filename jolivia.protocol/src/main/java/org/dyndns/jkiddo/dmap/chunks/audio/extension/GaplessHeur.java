package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class GaplessHeur extends UIntChunk
{
	public GaplessHeur()
	{
		this(0);
	}

	public GaplessHeur(int b)
	{
		super("aeGH", "com.apple.itunes.gapless-heur", b);
	}
}
