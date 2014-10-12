package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.cmrl)
public class UnknownRL extends BooleanChunk {
	public UnknownRL() {
		this(false);
	}

	public UnknownRL(boolean value) {
		super("cmrl", "unknown.rl", value);
	}

}
