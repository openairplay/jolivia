package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class UnknownCAPR extends UIntChunk{
	public UnknownCAPR() {
		this(0);
	}
	public UnknownCAPR(int value) {
		super("capr", "unknown-capr", value);
	}
}
