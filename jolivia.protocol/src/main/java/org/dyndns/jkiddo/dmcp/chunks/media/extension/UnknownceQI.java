package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ceQI)
public class UnknownceQI extends UIntChunk  {
	public UnknownceQI() {
		this(0);
	}

	public UnknownceQI(int i) {
		super("ceQI", "unknown", i);
	}
}
