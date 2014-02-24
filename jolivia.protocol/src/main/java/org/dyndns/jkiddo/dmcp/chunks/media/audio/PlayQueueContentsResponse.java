package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ceQR)
public class PlayQueueContentsResponse extends ContainerChunk {
	public PlayQueueContentsResponse() {
		super("ceQR", "com.apple.itunes.playqueue-contents-response");
	}
}
