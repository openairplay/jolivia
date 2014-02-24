package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeND)
public class NonDRMUserId extends ULongChunk
{
	public NonDRMUserId()
	{
		this(0);
	}

	public NonDRMUserId(int b)
	{
		super("aeND", "com.apple.itunes.non-drm-user-id", b);
	}
}
