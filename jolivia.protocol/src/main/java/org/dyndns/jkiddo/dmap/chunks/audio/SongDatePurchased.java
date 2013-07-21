package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.DateChunk;

public class SongDatePurchased extends DateChunk
{
	public SongDatePurchased()
	{
		this(0);
	}

	public SongDatePurchased(int i)
	{
		super("asdp", "daap.songdatepurchased", i);
	}
}
