package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class UnknownAC extends UIntChunk {
	public UnknownAC() {
		this(0);
	}

	public UnknownAC(int value) {
		super("agac", "com.apple.itunes.unknown-ac", value);
	}
}
