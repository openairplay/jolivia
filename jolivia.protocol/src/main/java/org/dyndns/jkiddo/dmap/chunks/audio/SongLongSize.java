package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

public class SongLongSize extends ULongChunk
{
	public SongLongSize()
	{
		this(0);
	}

	public SongLongSize(int v)
	{
		super("asls", "daap.songlongsize", v);
	}
}
