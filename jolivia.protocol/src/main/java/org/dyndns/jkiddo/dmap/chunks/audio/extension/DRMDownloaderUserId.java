package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

@DMAPAnnotation(type=DmapChunkDefinition.aeDL)
public class DRMDownloaderUserId extends ULongChunk {
	public DRMDownloaderUserId() {
		this(0);
	}

	public DRMDownloaderUserId(final int i) {
		super("aeDL", "com.apple.itunes.drm-downloader-user-id", i);
	}
}
