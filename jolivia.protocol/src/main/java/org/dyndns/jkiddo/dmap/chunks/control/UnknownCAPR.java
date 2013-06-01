package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.VersionChunk;

public class UnknownCAPR extends VersionChunk {
	public UnknownCAPR() {
		this(0);
	}

	public UnknownCAPR(int value) {
		super("capr", "unknown-capr", value);
	}

	public UnknownCAPR(int major, int minor, int patch) {
		super("capr", "unknown-capr", major, minor, patch);
	}
}
