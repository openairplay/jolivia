package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.msed)
public class SupportsPlaylistEdit extends BooleanChunk
{
	public SupportsPlaylistEdit()
	{
		this(false);
	}

	public SupportsPlaylistEdit(boolean i)
	{
		super("msed", "com.apple.itunes.unknown-ed", i);
	}
}
