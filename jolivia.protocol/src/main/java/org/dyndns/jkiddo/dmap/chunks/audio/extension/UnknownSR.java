package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeSR)
public class UnknownSR extends BooleanChunk
{
	public UnknownSR()
	{
		this(false);
	}

	public UnknownSR(boolean i)
	{
		super("aeSR", "com.apple.itunes.unknown-SR", i);
	}
}
