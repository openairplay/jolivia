package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeGH)
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
