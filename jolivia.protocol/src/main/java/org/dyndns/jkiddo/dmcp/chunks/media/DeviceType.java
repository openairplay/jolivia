package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class DeviceType extends StringChunk {

	public DeviceType() {
		this("");
	}

	public DeviceType(String value) {
		super("cmty", "unknown-ty", value);
	}
}
