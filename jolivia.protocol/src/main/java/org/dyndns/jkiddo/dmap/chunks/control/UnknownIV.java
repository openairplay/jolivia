package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownIV extends BooleanChunk {
	public UnknownIV() {
		this(false);
	}

	public UnknownIV(boolean value) {
		super("caiv", "com.apple.itunes.unknown-iv", value);
	}
}
