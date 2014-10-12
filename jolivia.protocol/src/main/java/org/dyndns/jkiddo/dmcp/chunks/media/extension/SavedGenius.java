package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.ceSG)
public class SavedGenius extends BooleanChunk{

	public SavedGenius() {
		this(false);
	}
	public SavedGenius(boolean value) {
		super("ceSG", "com.apple.itunes.saved-genius", value);
	}
}
