package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQg)
public class PlayQueueGenre extends StringChunk  {
	public PlayQueueGenre() {
		this("");
	}

	public PlayQueueGenre(String i) {
		super("ceQg", "com.apple.itunes.playqueue-genre", i);
	}
}
