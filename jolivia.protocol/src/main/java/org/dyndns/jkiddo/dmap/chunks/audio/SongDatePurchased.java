package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.DateChunk;

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
