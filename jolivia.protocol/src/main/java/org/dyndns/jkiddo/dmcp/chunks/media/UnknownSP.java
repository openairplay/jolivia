package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.cmsp)
public class UnknownSP extends BooleanChunk{

	public UnknownSP() {
		this(false);
	}

	public UnknownSP(boolean value) {
		super("cmsp","unknown-sp", value);
	}
}
