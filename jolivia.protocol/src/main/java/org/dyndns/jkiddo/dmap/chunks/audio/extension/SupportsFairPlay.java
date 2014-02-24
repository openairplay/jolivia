package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeFP)
public class SupportsFairPlay extends UByteChunk
{
	public static final int UNKNOWN_VALUE = 2;
	
	public SupportsFairPlay()
	{
		this(0);
	}

	public SupportsFairPlay(int i)
	{
		super("aeFP", "com.apple.itunes.unknown-FP", i);
	}
}
