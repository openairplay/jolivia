package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class SortName extends StringChunk
{
	public SortName()
	{
		this("");
	}

	public SortName(String value)
	{
		super("assn", "daap.sortname", value);
	}
}
