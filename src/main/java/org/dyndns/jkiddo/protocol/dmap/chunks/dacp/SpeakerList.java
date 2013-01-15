package org.dyndns.jkiddo.protocol.dmap.chunks.dacp;

import org.dyndns.jkiddo.protocol.dmap.chunks.ContainerChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.Dictionary;

public class SpeakerList extends ContainerChunk
{
	public SpeakerList()
	{
		super("casp", "com.apple.itunes.unknown-sp");
	}
	
	public Iterable<Dictionary> getDictionaries()
	{
		return getMultipleChunks(Dictionary.class);
	}
}
