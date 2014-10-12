package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.assu)
public class SortAlbum extends StringChunk
{
	public SortAlbum()
	{
		this("");
	}

	public SortAlbum(String b)
	{
		super("assu", "daap.sortalbum", b);
	}
}
