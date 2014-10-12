package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQm)
public class UnknownQm extends UIntChunk  {
	public UnknownQm() {
		this(0);
	}

	public UnknownQm(int i) {
		super("ceQm", "unknown", i);
	}
}
