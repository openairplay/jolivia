package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.VersionChunk;

public class MediaControlProtocolVersion extends VersionChunk
{

	public MediaControlProtocolVersion()
	{
		this(0);
	}

	public MediaControlProtocolVersion(int value)
	{
		super("cmpr", "dmcp.protocolversion", value);
	}

	public MediaControlProtocolVersion(int major, int minor, int patch)
	{
		super("cmpr", "dmcp.protocolversion", major, minor, patch);
	}
}
