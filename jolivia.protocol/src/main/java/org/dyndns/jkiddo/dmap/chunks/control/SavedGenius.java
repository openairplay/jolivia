package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;

public class SavedGenius extends BooleanChunk{

	public SavedGenius() {
		this(false);
	}
	public SavedGenius(boolean value) {
		super("ceSG", "com.apple.itunes.saved-genius", value);
	}
}
