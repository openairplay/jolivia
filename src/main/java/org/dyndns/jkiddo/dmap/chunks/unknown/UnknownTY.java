package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownTY extends StringChunk {

	public UnknownTY() {
		this("");
	}

	public UnknownTY(String value) {
		super("cmty", "unknown-ty", value);
	}
}
