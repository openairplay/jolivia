package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.assl)
public class SortAlbumArtist extends StringChunk
{
	public SortAlbumArtist()
	{
		this("");
	}

	public SortAlbumArtist(String value)
	{
		super("assl", "daap.sortalbumartist", value);
	}
}
