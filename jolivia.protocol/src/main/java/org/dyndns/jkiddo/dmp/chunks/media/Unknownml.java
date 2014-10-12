package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.msml)
public class Unknownml extends ContainerChunk
{
	public Unknownml()
	{
		super("msml", "com.apple.itunes.unknown-ml");
	}
}
