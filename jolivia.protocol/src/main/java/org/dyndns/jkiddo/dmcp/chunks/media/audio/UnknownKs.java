package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.caks)
public class UnknownKs extends UByteChunk{

	public UnknownKs() {
		this(0);
	}
	public UnknownKs(int value) {
		super("caks", "unknown.ss", value);
	}

}
