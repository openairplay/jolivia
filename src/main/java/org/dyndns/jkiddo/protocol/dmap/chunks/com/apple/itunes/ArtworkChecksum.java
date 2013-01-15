package org.dyndns.jkiddo.protocol.dmap.chunks.com.apple.itunes;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class ArtworkChecksum extends UIntChunk
{

	public ArtworkChecksum()
	{
		this(0);
	}

	public ArtworkChecksum(int value)
	{
		super("aeCs", "com.apple.itunes.artworkchecksum", value);
	}
}
