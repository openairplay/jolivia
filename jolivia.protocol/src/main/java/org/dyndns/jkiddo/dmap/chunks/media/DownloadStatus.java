package org.dyndns.jkiddo.dmap.chunks.media;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class DownloadStatus extends BooleanChunk {
	public DownloadStatus() {
		this(false);
	}

	public DownloadStatus(boolean value) {
		super("mdst", "dmap.downloadstatus", value);
	}
}
