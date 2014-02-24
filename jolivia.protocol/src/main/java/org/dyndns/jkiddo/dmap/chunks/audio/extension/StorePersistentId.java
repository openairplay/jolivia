package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeSE)
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
