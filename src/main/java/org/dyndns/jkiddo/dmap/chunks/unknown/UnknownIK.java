package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownIK extends StringChunk {

	public UnknownIK() {
		this("");
	}

	public UnknownIK(String value) {
		super("cmik", "unknown-ik", value);
	}
}
