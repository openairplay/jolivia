package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class UnknownSG extends BooleanChunk{

	public UnknownSG() {
		this(false);
	}
	public UnknownSG(boolean value) {
		super("ceSG", "unknown.sg", value);
	}
}
