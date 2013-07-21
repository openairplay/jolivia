package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.ULongChunk;

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
