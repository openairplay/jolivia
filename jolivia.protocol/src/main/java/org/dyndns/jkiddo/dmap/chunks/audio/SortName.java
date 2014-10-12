package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.assn)
public class SortName extends StringChunk
{
	public SortName()
	{
		this("");
	}

	public SortName(String value)
	{
		super("assn", "daap.sortname", value);
	}
}
