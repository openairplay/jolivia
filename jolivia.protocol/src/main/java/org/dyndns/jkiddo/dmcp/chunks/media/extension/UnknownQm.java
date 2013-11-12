package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class UnknownQm extends UIntChunk  {
	public UnknownQm() {
		this(0);
	}

	public UnknownQm(int i) {
		super("ceQm", "unknown", i);
	}
}
