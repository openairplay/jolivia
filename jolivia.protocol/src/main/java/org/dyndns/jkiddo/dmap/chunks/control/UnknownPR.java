package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.VersionChunk;

public class UnknownPR extends VersionChunk
{

	public UnknownPR()
	{
		this(0);
	}

	public UnknownPR(int value)
	{
		super("cmpr", "unknown-pr", value);
	}

	public UnknownPR(int major, int minor, int patch)
	{
		super("cmpr", "unknown-pr", major, minor, patch);
	}
}
