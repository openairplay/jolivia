package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeCM)
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
