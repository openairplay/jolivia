package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class UnknownQh extends StringChunk  {
	public UnknownQh() {
		this("");
	}

	public UnknownQh(String i) {
		super("ceQh", "unknown", i);
	}
}
