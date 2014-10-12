package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeCU)
public class CloudUserID extends ULongChunk
{
	public CloudUserID()
	{
		this(0);
	}

	public CloudUserID(int i)
	{
		super("aeCU", "com.apple.itunes.cloud-user-id", i);
	}
}
