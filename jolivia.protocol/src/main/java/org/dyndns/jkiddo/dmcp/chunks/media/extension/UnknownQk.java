package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQk)
public class UnknownQk extends StringChunk  {
	public UnknownQk() {
		this("");
	}

	public UnknownQk(String i) {
		super("ceQk", "unknown", i);
	}
}
