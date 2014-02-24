package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.asss)
public class SortSeriesName extends StringChunk
{
	public SortSeriesName()
	{
		this("");
	}

	public SortSeriesName(String b)
	{
		super("asss", "daap.sortseriesname", b);
	}
}
