package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.caiv)
public class UnknownIV extends BooleanChunk {
	public UnknownIV() {
		this(false);
	}

	public UnknownIV(boolean value) {
		super("caiv", "com.apple.itunes.unknown-iv", value);
	}
}
