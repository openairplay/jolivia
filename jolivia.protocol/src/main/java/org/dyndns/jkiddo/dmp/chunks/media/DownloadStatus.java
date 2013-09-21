package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class DownloadStatus extends BooleanChunk {
	public DownloadStatus() {
		this(false);
	}

	public DownloadStatus(boolean value) {
		super("mdst", "dmap.downloadstatus", value);
	}
}
