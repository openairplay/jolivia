package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.VersionChunk;

public class AudioControlProtocolVersion extends VersionChunk {
	public AudioControlProtocolVersion() {
		this(0);
	}

	public AudioControlProtocolVersion(int value) {
		super("capr", "dacp.protocolversion", value);
	}

	public AudioControlProtocolVersion(int major, int minor, int patch) {
		super("capr", "dacp.protocolversion", major, minor, patch);
	}
}
