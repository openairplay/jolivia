package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class Xid extends StringChunk
{
	public Xid()
	{
		this("");
	}

	public Xid(String b)
	{
		super("aeXD", "com.apple.itunes.xid", b);
	}
}
