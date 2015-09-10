package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

@DMAPAnnotation(type=DmapChunkDefinition.aels)
public class LikedState extends BooleanChunk
{
	public LikedState()
	{
		this(false);
	}

	public LikedState(final boolean value)
	{
		super("aels", "com.apple.itunes.liked-state", value);
	}
}