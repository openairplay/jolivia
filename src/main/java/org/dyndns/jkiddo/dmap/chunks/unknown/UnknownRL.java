package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownRL extends BooleanChunk {
	public UnknownRL() {
		this(false);
	}

	public UnknownRL(boolean value) {
		super("cmrl", "unknown.rl", value);
	}

}
