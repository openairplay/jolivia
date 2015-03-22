package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

@DMAPAnnotation(type=DmapChunkDefinition.aeFA)
public class DRMFamilyId extends ULongChunk{

	public DRMFamilyId()
	{
		this(0);
	}
	
	public DRMFamilyId(final int i) {
		super("aeFA", "com.apple.itunes.drm-family-id", i);
	}

}
