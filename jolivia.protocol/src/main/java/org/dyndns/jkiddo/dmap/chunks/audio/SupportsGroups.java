package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.UShortChunk;

@DMAPAnnotation(type=DmapChunkDefinition.asgr)
public class SupportsGroups extends UShortChunk
{
	public static final int SUPPORTS_ARTISTS_GROUPS = 1;
	public static final int SUPPORTS_ALBUM_GROUPS = 2;
	public static final int SUPPORTS_ARTISTS_AND_ALBUM_GROUPS = SUPPORTS_ARTISTS_GROUPS + SUPPORTS_ALBUM_GROUPS;
	public static final int NO_GROUPS = 0;
	public SupportsGroups()
	{
		this(0);
	}

	public SupportsGroups(final int i)
	{
		super("asgr", "daap.supportsgroups", i);
	}
}
