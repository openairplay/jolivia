package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class UnknownRL extends BooleanChunk {
	public UnknownRL() {
		this(false);
	}

	public UnknownRL(boolean value) {
		super("cmrl", "unknown.rl", value);
	}

}
