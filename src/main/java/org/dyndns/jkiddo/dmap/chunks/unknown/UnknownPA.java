package org.dyndns.jkiddo.dmap.chunks.unknown;
import java.util.Collection;

import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.ContainerChunk;

public class UnknownPA extends ContainerChunk {

	public UnknownPA() {
		super("cmpa", "unknown.pa");
	}

	public Collection<? extends Chunk> getChuncks() {
		return collection;
	}
	
	public UnknownPG getUnknownPG()
	{
		return getSingleChunk(UnknownPG.class);
	}
	
	public UnknownNM getUnknownNM()
	{
		return getSingleChunk(UnknownNM.class);
	}
	
	public UnknownTY getUnknownTY()
	{
		return getSingleChunk(UnknownTY.class);
	}
}
