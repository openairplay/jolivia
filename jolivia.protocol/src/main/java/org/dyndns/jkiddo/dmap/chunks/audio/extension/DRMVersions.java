package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class DRMVersions extends UIntChunk
{
	public DRMVersions()
	{
		this(0);
	}

	public DRMVersions(int b)
	{
		super("aeDV", "com.apple.itunes.drm-versions", b);
	}
}
