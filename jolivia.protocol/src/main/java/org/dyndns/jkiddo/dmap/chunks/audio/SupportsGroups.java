package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UShortChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.asgr)
public class SupportsGroups extends UShortChunk
{
	public SupportsGroups()
	{
		this(0);
	}

	public SupportsGroups(int i)
	{
		super("asgr", "daap.supportsgroups", i);
	}
}
