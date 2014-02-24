package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeCS)
public class UnknownCS extends UIntChunk
{

	public UnknownCS()
	{
		this(0);
	}

	public UnknownCS(int i)
	{
		super("aeCS", "com.apple.itunes.unknown-CS", i);
	}
}
