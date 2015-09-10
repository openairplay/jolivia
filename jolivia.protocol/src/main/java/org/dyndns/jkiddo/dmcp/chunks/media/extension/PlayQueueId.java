package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.StringChunk;

@DMAPAnnotation(type=DmapChunkDefinition.ceQs)
public class PlayQueueId extends StringChunk  {
	public PlayQueueId() {
		this("");
	}

	public PlayQueueId(final String i) {
		super("ceQs", "com.apple.itunes.playqueue-id", i);
	}
}
