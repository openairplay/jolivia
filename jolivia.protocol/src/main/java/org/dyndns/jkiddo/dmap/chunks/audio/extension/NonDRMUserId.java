package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeND)
public class NonDRMUserId extends ULongChunk
{
	public NonDRMUserId()
	{
		this(0);
	}

	public NonDRMUserId(int b)
	{
		super("aeND", "com.apple.itunes.non-drm-user-id", b);
	}
}
