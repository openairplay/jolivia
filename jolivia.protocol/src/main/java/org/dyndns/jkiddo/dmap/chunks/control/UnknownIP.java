package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownIP extends BooleanChunk {
	public UnknownIP() {
		this(false);
	}

	public UnknownIP(boolean value) {
		super("caip", "com.apple.itunes.unknown-ip", value);
	}
}
