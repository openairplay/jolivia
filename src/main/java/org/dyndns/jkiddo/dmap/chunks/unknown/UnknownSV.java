package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownSV extends StringChunk{

	public UnknownSV() {
		this("");
	}
	public UnknownSV(String value) {
		super("cmsv", "unknown.sv", value);
	}

}
