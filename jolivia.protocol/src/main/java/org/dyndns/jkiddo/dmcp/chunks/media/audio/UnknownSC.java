package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

public class UnknownSC extends UByteChunk {

	public UnknownSC() {
		this(0);
	}
	public UnknownSC(int value) {
		super("casc", "unknown.ss", value);
	}

}
