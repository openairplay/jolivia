package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.cmty)
public class DeviceType extends StringChunk {

	public DeviceType() {
		this("");
	}

	public DeviceType(String value) {
		super("cmty", "unknown-ty", value);
	}
}
