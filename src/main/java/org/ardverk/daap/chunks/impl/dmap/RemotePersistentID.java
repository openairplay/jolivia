package org.ardverk.daap.chunks.impl.dmap;

import org.ardverk.daap.chunks.ULongChunk;

public class RemotePersistentID extends ULongChunk
{
	public RemotePersistentID()
	{
		this(0);
	}
	public RemotePersistentID(long value)
	{
		super("mrpr", "dmap.remotepersistentid", value);
	}
}
