package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeDR)
public class DRMUserId extends ULongChunk
{
	public DRMUserId()
	{
		this(0);
	}

	public DRMUserId(int b)
	{
		super("aeDR", "com.apple.itunes.drm-user-id", b);
	}
}
