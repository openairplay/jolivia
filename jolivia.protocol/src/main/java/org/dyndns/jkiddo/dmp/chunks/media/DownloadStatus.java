package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.mdst)
public class DownloadStatus extends BooleanChunk {
	public DownloadStatus() {
		this(false);
	}

	public DownloadStatus(boolean value) {
		super("mdst", "dmap.downloadstatus", value);
	}
}
