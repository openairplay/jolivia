package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownCAPR extends StringChunk{
	public UnknownCAPR() {
		this("");
	}
	public UnknownCAPR(String value) {
		super("capr", "unknown-capr", value);
	}
}
