package org.dyndns.jkiddo.dmap.chunks.unknown;
import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownNM extends StringChunk {

	public UnknownNM() {
		this("");
	}

	public UnknownNM(String value) {

		super("cmnm", "unknown-nm", value);
	}

}
