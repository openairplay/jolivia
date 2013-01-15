package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.ContainerChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.Listing;

public class UnknownAL extends ContainerChunk
{
	public UnknownAL()
	{
		super("agal", "com.apple.itunes.unknown-al");
	}
	
	public Listing getListing()
	{
		return getSingleChunk(Listing.class);
	}
}
