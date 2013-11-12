package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class PlayQueueArtist extends StringChunk  {
	public PlayQueueArtist() {
		this("");
	}

	public PlayQueueArtist(String i) {
		super("ceQr", "com.apple.itunes.playqueue-artist", i);
	}
}
