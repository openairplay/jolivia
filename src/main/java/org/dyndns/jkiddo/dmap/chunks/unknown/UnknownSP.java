package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSP extends BooleanChunk{

	public UnknownSP() {
		this(false);
	}

	public UnknownSP(boolean value) {
		super("cmsp","unknown-sp", value);
	}
}
