package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.agac)
public class GroupAlbumCount extends UIntChunk {
	public GroupAlbumCount() {
		this(0);
	}

	public GroupAlbumCount(int value) {
		super("agac", "daap.groupalbumcount", value);
	}
}
