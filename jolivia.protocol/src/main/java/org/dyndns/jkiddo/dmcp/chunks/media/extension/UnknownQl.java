package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class UnknownQl extends StringChunk  {
	public UnknownQl() {
		this("");
	}

	public UnknownQl(String i) {
		super("ceQl", "unknown", i);
	}
}
