package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

public class CloudStatus extends UByteChunk
{
	public CloudStatus()
	{
		this(0);
	}

	public CloudStatus(int i)
	{
		super("aeCM", "com.apple.itunes.cloud-status", i);
	}
}
