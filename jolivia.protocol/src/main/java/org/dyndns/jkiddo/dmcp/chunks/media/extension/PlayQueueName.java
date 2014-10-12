package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQn)
public class PlayQueueName extends StringChunk  {
	public PlayQueueName() {
		this("");
	}

	public PlayQueueName(String i) {
		super("ceQn", "com.apple.itunes.playqueue-name", i);
	}
}
