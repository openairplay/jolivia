package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.cass)
public class UnknownSS extends BooleanChunk{

	public UnknownSS() {
		this(false);
	}
	public UnknownSS(boolean value) {
		super("cass", "unknown.ss", value);
	}

}
