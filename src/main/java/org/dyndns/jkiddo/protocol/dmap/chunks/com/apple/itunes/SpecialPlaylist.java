package org.dyndns.jkiddo.protocol.dmap.chunks.com.apple.itunes;

import org.dyndns.jkiddo.protocol.dmap.chunks.UByteChunk;

public class SpecialPlaylist extends UByteChunk
{
	public SpecialPlaylist()
	{
		this(0);
	}

	public SpecialPlaylist(int mode)
	{
		super("aePS", "com.apple.itunes.special-playlist", mode);
	}
}
