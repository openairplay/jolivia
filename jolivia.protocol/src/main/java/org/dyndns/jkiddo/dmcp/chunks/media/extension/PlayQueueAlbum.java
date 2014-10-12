package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQa)
public class PlayQueueAlbum extends StringChunk  {
	public PlayQueueAlbum() {
		this("");
	}

	public PlayQueueAlbum(String i) {
		super("ceQa", "com.apple.itunes.playqueue-album", i);
	}
}
