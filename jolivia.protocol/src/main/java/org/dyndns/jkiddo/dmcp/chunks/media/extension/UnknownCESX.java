package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

public class UnknownCESX extends ULongChunk {
	public UnknownCESX() {
		this(0);
	}

	public UnknownCESX(long value) {
		super("ceSX", "unknown.sx", value);
	}

}
