package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

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
