package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownIK extends BooleanChunk {

	public UnknownIK() {
		this(false);
	}

	public UnknownIK(boolean value) {
		super("cmik", "unknown-ik", value);
	}
}
