package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.UByteChunk;

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
