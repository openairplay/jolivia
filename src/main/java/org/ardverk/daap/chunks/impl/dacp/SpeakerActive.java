package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.BooleanChunk;

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
