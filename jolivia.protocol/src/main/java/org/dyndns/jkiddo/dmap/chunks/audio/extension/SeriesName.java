package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeSN)
public class SeriesName extends StringChunk
{
	public SeriesName()
	{
		this("");
	}

	public SeriesName(String s)
	{
		super("aeSN", "com.apple.itunes.series-name", s);
	}
}
