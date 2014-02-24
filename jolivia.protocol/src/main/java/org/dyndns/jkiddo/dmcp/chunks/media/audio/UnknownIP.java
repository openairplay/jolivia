package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.caip)
public class UnknownIP extends BooleanChunk {
	public UnknownIP() {
		this(false);
	}

	public UnknownIP(boolean value) {
		super("caip", "com.apple.itunes.unknown-ip", value);
	}
}
