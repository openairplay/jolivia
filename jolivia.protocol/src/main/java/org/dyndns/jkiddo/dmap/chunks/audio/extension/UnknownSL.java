package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeSL)
public class UnknownSL extends BooleanChunk
{
	public UnknownSL()
	{
		this(false);
	}

	public UnknownSL(boolean i)
	{
		super("aeSL", "com.apple.itunes.unknown-SL", i);
	}
}
