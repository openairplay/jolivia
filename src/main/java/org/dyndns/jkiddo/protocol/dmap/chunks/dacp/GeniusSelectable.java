package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

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
