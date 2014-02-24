package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.assa)
public class SortArtist extends StringChunk
{
	public SortArtist()
	{
		this("");
	}

	public SortArtist(String value)
	{
		super("assa", "daap.sortartist", value);
	}
}
