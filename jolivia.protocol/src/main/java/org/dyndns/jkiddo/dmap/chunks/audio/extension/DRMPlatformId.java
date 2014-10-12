package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeDP)
public class DRMPlatformId extends UIntChunk
{
	public DRMPlatformId()
	{
		this(0);
	}

	public DRMPlatformId(int value)
	{
		super("aeDP", "com.apple.itunes.drm-platform-id", value);
	}
}
