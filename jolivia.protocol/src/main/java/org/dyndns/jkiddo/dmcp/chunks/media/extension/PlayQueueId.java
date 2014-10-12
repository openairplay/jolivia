package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQs)
public class PlayQueueId extends ULongChunk  {
	public PlayQueueId() {
		this(0);
	}

	public PlayQueueId(int i) {
		super("ceQs", "com.apple.itunes.playqueue-id", i);
	}
}
