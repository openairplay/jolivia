package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.assc)
public class SortComposer extends StringChunk
{
	public SortComposer()
	{
		this("");
	}

	public SortComposer(String b)
	{
		super("assc", "daap.sortcomposer", b);
	}
}
