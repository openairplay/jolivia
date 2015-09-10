package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeCM)
public class CloudMatchType extends UByteChunk
{
	public CloudMatchType()
	{
		this(0);
	}

	public CloudMatchType(int i)
	{
		super("aeCM", "com.apple.itunes.cloud-status", i);
	}
}
