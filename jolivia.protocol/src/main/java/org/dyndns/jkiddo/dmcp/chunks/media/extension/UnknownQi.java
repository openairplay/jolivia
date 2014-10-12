package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQi)
public class UnknownQi extends UIntChunk  {
	public UnknownQi() {
		this(0);
	}

	public UnknownQi(int i) {
		super("ceQi", "unknown", i);
	}
}
