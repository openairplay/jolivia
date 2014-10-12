package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQR)
public class PlayQueueContentsResponse extends ContainerChunk {
	public PlayQueueContentsResponse() {
		super("ceQR", "com.apple.itunes.playqueue-contents-response");
	}
}
