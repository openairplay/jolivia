package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ceQg)
public class PlayQueueGenre extends StringChunk  {
	public PlayQueueGenre() {
		this("");
	}

	public PlayQueueGenre(String i) {
		super("ceQg", "com.apple.itunes.playqueue-genre", i);
	}
}
