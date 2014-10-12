package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.cads)
public class UnknownDS extends UIntChunk
{
	public UnknownDS()
	{
		this(0);
	}
	
	public UnknownDS(int b)
	{
		super("cads","unknown-ds", b);
	}
}
