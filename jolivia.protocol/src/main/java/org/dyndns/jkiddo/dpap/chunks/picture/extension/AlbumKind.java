package org.dyndns.jkiddo.dpap.chunks.picture.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class AlbumKind extends UIntChunk
{
	public AlbumKind()
	{
		this(0);
	}

	public AlbumKind(long kind)
	{
		super("peak", "com.apple.itunes.photos.album-kind", kind);
	}
}
