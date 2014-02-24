package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.casc)
public class UnknownSC extends UByteChunk {

	public UnknownSC() {
		this(0);
	}
	public UnknownSC(int value) {
		super("casc", "unknown.ss", value);
	}

}
