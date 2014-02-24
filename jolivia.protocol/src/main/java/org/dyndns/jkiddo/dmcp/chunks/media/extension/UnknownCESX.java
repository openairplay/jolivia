package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ceSX)
public class UnknownCESX extends ULongChunk {
	public UnknownCESX() {
		this(0);
	}

	public UnknownCESX(long value) {
		super("ceSX", "unknown.sx", value);
	}

}
