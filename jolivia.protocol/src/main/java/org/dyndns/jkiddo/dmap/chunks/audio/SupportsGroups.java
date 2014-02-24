package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UShortChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.asgr)
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
