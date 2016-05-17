package org.dyndns.jkiddo.dmp.chunks.unknown;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

@DMAPAnnotation(type=DmapChunkDefinition.ajal, hash = 1634361708)
public class UnknowncA extends UByteChunk {

	public UnknowncA() {
		this(0);
	}

	public UnknowncA(final int value) {
		super("ajal", "com.apple.itunes.unknown-al", value);
	}
}
