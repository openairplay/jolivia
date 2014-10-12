package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceQh)
public class UnknownQh extends StringChunk  {
	public UnknownQh() {
		this("");
	}

	public UnknownQh(String i) {
		super("ceQh", "unknown", i);
	}
}
