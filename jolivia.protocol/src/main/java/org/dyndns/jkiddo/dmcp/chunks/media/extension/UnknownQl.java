package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ceQl)
public class UnknownQl extends StringChunk  {
	public UnknownQl() {
		this("");
	}

	public UnknownQl(String i) {
		super("ceQl", "unknown", i);
	}
}
