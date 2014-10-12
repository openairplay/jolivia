package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.DateChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.asdp)
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
