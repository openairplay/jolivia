package org.dyndns.jkiddo.dpap.chunks.picture.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class KeyImageId extends UIntChunk
{
	public KeyImageId()
	{
		this(0);
	}

	public KeyImageId(long id)
	{
		super("peki", "com.apple.itunes.photos.key-image-id", id);
	}
}
