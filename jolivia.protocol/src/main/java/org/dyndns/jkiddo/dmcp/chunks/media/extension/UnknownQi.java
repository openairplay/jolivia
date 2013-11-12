package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class UnknownQi extends UIntChunk  {
	public UnknownQi() {
		this(0);
	}

	public UnknownQi(int i) {
		super("ceQi", "unknown", i);
	}
}
