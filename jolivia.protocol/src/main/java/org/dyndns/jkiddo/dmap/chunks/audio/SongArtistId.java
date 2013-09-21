package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

public class SongArtistId extends ULongChunk {
	public SongArtistId() {
		this(0);
	}

	public SongArtistId(long value) {
		super("asri", "daap.songartistid", value);
	}
}
