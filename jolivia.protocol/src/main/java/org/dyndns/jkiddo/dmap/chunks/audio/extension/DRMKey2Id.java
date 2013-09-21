package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

public class DRMKey2Id extends ULongChunk
{
	public DRMKey2Id()
	{
		this(0);
	}

	public DRMKey2Id(int b)
	{
		super("aeK2", "com.apple.itunes.drm-key2-id", b);
	}
}
