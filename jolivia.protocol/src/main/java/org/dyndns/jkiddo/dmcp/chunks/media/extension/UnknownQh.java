package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ceQh)
public class UnknownQh extends StringChunk  {
	public UnknownQh() {
		this("");
	}

	public UnknownQh(String i) {
		super("ceQh", "unknown", i);
	}
}
