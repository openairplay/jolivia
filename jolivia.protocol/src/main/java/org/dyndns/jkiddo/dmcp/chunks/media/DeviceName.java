package org.dyndns.jkiddo.dmcp.chunks.media;
import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.cmnm)
public class DeviceName extends StringChunk {

	public DeviceName() {
		this("");
	}

	public DeviceName(String value) {

		super("cmnm", "unknown-nm", value);
	}

}
