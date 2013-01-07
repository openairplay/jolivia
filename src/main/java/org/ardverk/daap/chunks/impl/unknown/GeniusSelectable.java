package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UByteChunk;

public class GeniusSelectable extends UByteChunk
{
	public GeniusSelectable()
	{
		this(0);
	}

	public GeniusSelectable(int value)
	{
		super("ceGS", "com.apple.itunes.genius-selectable", value);
	}

}
