package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

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
