package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownOV extends BooleanChunk {
	public UnknownOV() {
		this(false);
	}

	public UnknownOV(boolean value) {
		super("caov", "unknown.ov", value);
	}

}
