package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownSS extends StringChunk{

	public UnknownSS() {
		this("");
	}
	public UnknownSS(String value) {
		super("cass", "unknown.ss", value);
	}

}
