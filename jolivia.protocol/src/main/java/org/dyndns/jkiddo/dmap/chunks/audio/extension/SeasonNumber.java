package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeSU)
public class SeasonNumber extends UIntChunk
{
	public SeasonNumber()
	{
		this(0);
	}

	public SeasonNumber(int b)
	{
		super("aeSU", "com.apple.itunes.season-num", b);
	}
}
