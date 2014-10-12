package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.mscu)
public class UnknownCU extends ULongChunk
{
	public UnknownCU()
	{
		this(0);

	}

	public UnknownCU(int b)
	{
		super("mscu", "unknown-cu", b);
	}
}
