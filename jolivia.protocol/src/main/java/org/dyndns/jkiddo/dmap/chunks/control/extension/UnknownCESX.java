package org.dyndns.jkiddo.dmap.chunks.control.extension;

import org.dyndns.jkiddo.dmap.chunks.ULongChunk;

public class UnknownCESX extends ULongChunk {
	public UnknownCESX() {
		this(0);
	}

	public UnknownCESX(long value) {
		super("ceSX", "unknown.sx", value);
	}

}
