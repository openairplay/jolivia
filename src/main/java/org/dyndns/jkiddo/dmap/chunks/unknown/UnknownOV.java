package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownOV extends StringChunk {
	public UnknownOV() {
		this("");
	}

	public UnknownOV(String value) {
		super("caov", "unknown.ov", value);
	}

}
