package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

public class CloudID extends ULongChunk
{
	public CloudID()
	{
		this(0);
	}

	public CloudID(int value)
	{
		super("aeCd", "com.apple.itunes.cloud-id", value);
	}

}
