package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.BooleanChunk;

public class SpeakerActive extends BooleanChunk
{
	public SpeakerActive()
	{
		this(false);
	}

	public SpeakerActive(boolean value)
	{
		super("caia", "com.apple.itunes.unknown-ia", value);
	}

}
