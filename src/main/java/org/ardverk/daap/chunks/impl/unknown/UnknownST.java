package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.Chunk;
import org.ardverk.daap.chunks.ContainerChunk;

public class UnknownST extends ContainerChunk
{
	public UnknownST()
	{
		super("cmst", "com.apple.itunes.unknown-st");
	}

	public <T extends Chunk> T getSpecificChunk(Class<T> clazz)
	{
		return getSingleChunk(clazz);
	}
}
