package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class SortComposer extends StringChunk
{
	public SortComposer()
	{
		this("");
	}

	public SortComposer(String b)
	{
		super("assc", "daap.sortcomposer", b);
	}
}
