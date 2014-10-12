package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.VersionChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.capr)
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
