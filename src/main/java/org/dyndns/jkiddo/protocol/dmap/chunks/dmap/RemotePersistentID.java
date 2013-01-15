package org.dyndns.jkiddo.protocol.dmap.chunks.dmap;

import org.dyndns.jkiddo.protocol.dmap.chunks.ULongChunk;

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
