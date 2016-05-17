package org.dyndns.jkiddo.dmp.chunks.unknown;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

@DMAPAnnotation(type=DmapChunkDefinition.ajcA, hash = 1634362177)
public class Unknownal extends UByteChunk {

	public Unknownal() {
		this(0);
	}
	
	public Unknownal(final int value) {
		super("ajcA", "com.apple.itunes.unknown-cA", value);
	}

}
