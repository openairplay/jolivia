package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeCK)
public class CloudLibraryKind extends UByteChunk
{
	public CloudLibraryKind()
	{
		this(0);
	}

	public CloudLibraryKind(int i)
	{
		super("aeCK", "com.apple.itunes.cloud-library-kind", i);
	}
}
