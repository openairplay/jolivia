package org.dyndns.jkiddo.protocol.dmap.chunks.com.apple.itunes;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class EMediaKind extends UIntChunk
{
	public EMediaKind()
	{
		this(0);
	}

	public EMediaKind(int value)
	{
		super("aeMk", "com.apple.itunes.extended-media-kind", value);
	}
}
