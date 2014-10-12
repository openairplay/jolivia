package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.caov)
public class UnknownOV extends BooleanChunk {
	public UnknownOV() {
		this(false);
	}

	public UnknownOV(boolean value) {
		super("caov", "unknown.ov", value);
	}

}
