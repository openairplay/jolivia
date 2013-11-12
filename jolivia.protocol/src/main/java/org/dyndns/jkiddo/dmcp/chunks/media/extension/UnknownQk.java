package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class UnknownQk extends StringChunk  {
	public UnknownQk() {
		this("");
	}

	public UnknownQk(String i) {
		super("ceQk", "unknown", i);
	}
}
