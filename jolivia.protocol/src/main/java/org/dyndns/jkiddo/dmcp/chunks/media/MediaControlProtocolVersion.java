package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.VersionChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.cmpr)
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
