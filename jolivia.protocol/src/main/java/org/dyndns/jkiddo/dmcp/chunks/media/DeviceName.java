package org.dyndns.jkiddo.dmcp.chunks.media;
import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class DeviceName extends StringChunk {

	public DeviceName() {
		this("");
	}

	public DeviceName(String value) {

		super("cmnm", "unknown-nm", value);
	}

}
