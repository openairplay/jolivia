package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.ULongChunk;

public class StorePersistentId extends ULongChunk
{
	public StorePersistentId()
	{
		this(0);
	}

	public StorePersistentId(int b)
	{
		super("aeSE", "com.apple.itunes.store-pers-id", b);
	}
}
