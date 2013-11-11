package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;
import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class UnknownQS extends StringChunk  {
	public UnknownQS() {
		this("");
	}

	public UnknownQS(String i) {
		super("ceQS", "com.apple.itunes.playqueue-score", i);
	}
}
