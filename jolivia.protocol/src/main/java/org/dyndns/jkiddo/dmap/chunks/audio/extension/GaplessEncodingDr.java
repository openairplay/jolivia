package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeGD)
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
