package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class DeviceType extends StringChunk {

	public DeviceType() {
		this("");
	}

	public DeviceType(String value) {
		super("cmty", "unknown-ty", value);
	}
}
