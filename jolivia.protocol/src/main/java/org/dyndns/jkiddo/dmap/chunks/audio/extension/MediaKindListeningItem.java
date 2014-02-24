package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aemi)
public class MediaKindListeningItem extends ContainerChunk
{
	public MediaKindListeningItem()
	{
		super("aemi", "com.apple.itunes.media-kind-listing-item");
	}
}
