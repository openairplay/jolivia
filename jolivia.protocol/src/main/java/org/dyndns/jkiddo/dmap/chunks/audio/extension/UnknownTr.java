package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeTr)
public class UnknownTr extends BooleanChunk
{
	public UnknownTr()
	{
		this(false);
	}

	public UnknownTr(boolean i)
	{
		super("aeTr", "com.apple.itunes.unknown-Tr", i);
	}
}
