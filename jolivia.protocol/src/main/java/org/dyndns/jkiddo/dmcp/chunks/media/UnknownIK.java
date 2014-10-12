package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.cmik)
public class UnknownIK extends BooleanChunk {

	public UnknownIK() {
		this(false);
	}

	public UnknownIK(boolean value) {
		super("cmik", "unknown-ik", value);
	}
}
