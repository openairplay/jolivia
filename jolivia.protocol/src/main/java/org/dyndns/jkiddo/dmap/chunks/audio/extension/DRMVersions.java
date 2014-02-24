package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeDV)
public class DRMVersions extends UIntChunk
{
	public DRMVersions()
	{
		this(0);
	}

	public DRMVersions(int b)
	{
		super("aeDV", "com.apple.itunes.drm-versions", b);
	}
}
