package org.dyndns.jkiddo.dmcp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.cmsv)
public class UnknownSV extends BooleanChunk{

	public UnknownSV() {
		this(false);
	}
	public UnknownSV(boolean value) {
		super("cmsv", "unknown.sv", value);
	}

}
