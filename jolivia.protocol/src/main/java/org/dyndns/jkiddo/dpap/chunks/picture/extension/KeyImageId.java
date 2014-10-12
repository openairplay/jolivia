package org.dyndns.jkiddo.dpap.chunks.picture.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.peki)
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
