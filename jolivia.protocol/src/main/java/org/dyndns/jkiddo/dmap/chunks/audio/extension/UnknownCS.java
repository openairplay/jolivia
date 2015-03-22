package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

@DMAPAnnotation(type=DmapChunkDefinition.aeCS)
public class UnknownCS extends UIntChunk
{

	public UnknownCS()
	{
		this(0);
	}

	public UnknownCS(final int i)
	{
		super("aeCS", "com.apple.itunes.artworkchecksum", i);
	}
}
