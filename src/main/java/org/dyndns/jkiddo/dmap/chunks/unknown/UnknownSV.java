package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSV extends BooleanChunk{

	public UnknownSV() {
		this(false);
	}
	public UnknownSV(boolean value) {
		super("cmsv", "unknown.sv", value);
	}

}
