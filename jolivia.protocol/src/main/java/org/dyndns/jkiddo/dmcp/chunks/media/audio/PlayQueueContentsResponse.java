package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

public class PlayQueueContentsResponse extends ContainerChunk {
	public PlayQueueContentsResponse() {
		super("ceQR", "com.apple.itunes.playqueue-contents-response");
	}
}
