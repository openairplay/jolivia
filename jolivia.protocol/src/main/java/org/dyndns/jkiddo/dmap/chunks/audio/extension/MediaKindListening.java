package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeml)
public class MediaKindListening extends ContainerChunk
{
	public MediaKindListening()
	{
		super("aeml", "com.apple.itunes.media-kind-listing");
	}
}
