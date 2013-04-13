package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownCESX extends StringChunk {
	public UnknownCESX() {
		this("");
	}

	public UnknownCESX(String value) {
		super("ceSX", "unknown.sx", value);
	}

}
