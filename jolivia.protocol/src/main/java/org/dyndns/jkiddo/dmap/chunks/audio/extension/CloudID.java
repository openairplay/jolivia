package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeCd)
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
