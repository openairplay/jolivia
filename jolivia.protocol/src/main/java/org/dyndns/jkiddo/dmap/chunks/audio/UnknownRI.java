package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.ULongChunk;

public class UnknownRI extends ULongChunk {
	public UnknownRI() {
		this(0);
	}

	public UnknownRI(long value) {
		super("asri", "com.apple.itunes.unknown-ri", value);
	}
}
