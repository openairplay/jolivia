package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class GroupAlbumCount extends UIntChunk {
	public GroupAlbumCount() {
		this(0);
	}

	public GroupAlbumCount(int value) {
		super("agac", "daap.groupalbumcount", value);
	}
}
