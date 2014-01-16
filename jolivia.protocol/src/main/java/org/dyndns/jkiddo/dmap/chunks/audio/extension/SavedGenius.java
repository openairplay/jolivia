package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class SavedGenius extends BooleanChunk
{
	public SavedGenius()
	{
		this(false);
	}

	public SavedGenius(boolean i)
	{
		super("aeSG", "com.apple.itunes.saved-genius", i);
	}
}
