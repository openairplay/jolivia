package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

public class SavedGenius extends BooleanChunk{

	public SavedGenius() {
		this(false);
	}
	public SavedGenius(boolean value) {
		super("ceSG", "com.apple.itunes.saved-genius", value);
	}
}
