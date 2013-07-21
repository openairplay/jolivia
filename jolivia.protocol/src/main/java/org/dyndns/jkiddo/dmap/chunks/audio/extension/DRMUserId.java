package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.ULongChunk;

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
