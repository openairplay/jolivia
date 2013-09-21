package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class UnknownSV extends BooleanChunk{

	public UnknownSV() {
		this(false);
	}
	public UnknownSV(boolean value) {
		super("cmsv", "unknown.sv", value);
	}

}
