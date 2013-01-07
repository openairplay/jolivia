package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.ContainerChunk;
import org.ardverk.daap.chunks.impl.dmap.Dictionary;

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
