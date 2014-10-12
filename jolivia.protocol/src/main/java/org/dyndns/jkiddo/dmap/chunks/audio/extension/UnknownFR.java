package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeFR)
public class UnknownFR extends UByteChunk
{
	public UnknownFR()
	{
		this(0);
	}

	public UnknownFR(int i)
	{
		super("aeFR", "com.apple.itunes.unknown-FR", i);
	}
}
