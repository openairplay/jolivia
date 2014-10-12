package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeSL)
public class UnknownSL extends BooleanChunk
{
	public UnknownSL()
	{
		this(false);
	}

	public UnknownSL(boolean i)
	{
		super("aeSL", "com.apple.itunes.unknown-SL", i);
	}
}
