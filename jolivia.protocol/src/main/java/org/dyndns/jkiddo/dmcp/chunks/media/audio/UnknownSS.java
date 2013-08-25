package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSS extends BooleanChunk{

	public UnknownSS() {
		this(false);
	}
	public UnknownSS(boolean value) {
		super("cass", "unknown.ss", value);
	}

}
