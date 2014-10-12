package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeSX)
public class UnknownSX extends ULongChunk
{
	public UnknownSX()
	{
		this(0);
	}

	public UnknownSX(int i)
	{
		super("aeSX", "com.apple.itunes.unknown-SX", i);
	}
}
