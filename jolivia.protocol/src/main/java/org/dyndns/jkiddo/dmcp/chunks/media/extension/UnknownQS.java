package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

public class UnknownQS extends ContainerChunk  {
	public UnknownQS() {
		this("");
	}

	public UnknownQS(String i) {
		super("ceQS", "com.apple.itunes.playqueue-content-unknown");
	}
}
