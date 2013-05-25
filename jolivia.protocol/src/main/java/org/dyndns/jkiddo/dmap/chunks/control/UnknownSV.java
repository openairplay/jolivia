package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSV extends BooleanChunk{

	public UnknownSV() {
		this(false);
	}
	public UnknownSV(boolean value) {
		super("cmsv", "unknown.sv", value);
	}

}
