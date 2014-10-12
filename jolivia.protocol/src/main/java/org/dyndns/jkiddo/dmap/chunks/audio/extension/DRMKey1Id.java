package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeK1)
public class DRMKey1Id extends ULongChunk
{
	public DRMKey1Id()
	{
		this(0);
	}

	public DRMKey1Id(int b)
	{
		super("aeK1", "com.apple.itunes.drm-key1-id", b);
	}
}
