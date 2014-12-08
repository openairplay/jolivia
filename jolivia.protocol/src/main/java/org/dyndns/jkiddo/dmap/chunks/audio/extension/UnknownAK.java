package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

@DMAPAnnotation(type = DmapChunkDefinition.aeAK)
public class UnknownAK extends UIntChunk
{
	public UnknownAK()
	{
		this(0);
	}
	
	public UnknownAK(final int value)
	{
		super("aeAK", "com.apple.itunes.unknown-AK", value);
	}

}
