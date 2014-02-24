package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.asbk)
public class SortBookmarkable extends BooleanChunk
{
	public SortBookmarkable()
	{
		this(false);
	}

	public SortBookmarkable(boolean b)
	{
		super("asbk", "daap.bookmarkable", b);
	}
}
