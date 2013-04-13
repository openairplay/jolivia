package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownRL extends StringChunk {
	public UnknownRL() {
		this("");
	}

	public UnknownRL(String value) {
		super("cmrl", "unknown.rl", value);
	}

}
