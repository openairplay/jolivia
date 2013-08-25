package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownIV extends BooleanChunk {
	public UnknownIV() {
		this(false);
	}

	public UnknownIV(boolean value) {
		super("caiv", "com.apple.itunes.unknown-iv", value);
	}
}
