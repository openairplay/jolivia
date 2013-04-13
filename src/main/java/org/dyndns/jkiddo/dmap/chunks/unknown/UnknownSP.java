package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownSP extends StringChunk{

	public UnknownSP() {
		this("");
	}

	public UnknownSP(String value) {
		super("cmsp","unknown-sp", value);
	}

}
