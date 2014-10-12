package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.asri)
public class SongArtistId extends ULongChunk {
	public SongArtistId() {
		this(0);
	}

	public SongArtistId(long value) {
		super("asri", "daap.songartistid", value);
	}
}
