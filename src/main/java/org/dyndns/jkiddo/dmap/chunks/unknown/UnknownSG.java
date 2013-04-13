package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownSG extends StringChunk{

	public UnknownSG() {
		this("");
	}
	public UnknownSG(String value) {
		super("ceSG", "unknown.sg", value);
	}
}
