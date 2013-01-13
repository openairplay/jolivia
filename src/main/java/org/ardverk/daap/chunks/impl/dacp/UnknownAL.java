package org.ardverk.daap.chunks.impl.dacp;

import org.ardverk.daap.chunks.ContainerChunk;
import org.ardverk.daap.chunks.impl.dmap.Listing;

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
