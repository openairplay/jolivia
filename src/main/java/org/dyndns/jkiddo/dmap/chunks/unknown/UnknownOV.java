package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownOV extends BooleanChunk {
	public UnknownOV() {
		this(false);
	}

	public UnknownOV(boolean value) {
		super("caov", "unknown.ov", value);
	}

}
