package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.msed)
public class Unknowned extends BooleanChunk
{
	public Unknowned()
	{
		this(false);
	}

	public Unknowned(boolean i)
	{
		super("msed", "com.apple.itunes.unknown-ed", i);
	}
}
