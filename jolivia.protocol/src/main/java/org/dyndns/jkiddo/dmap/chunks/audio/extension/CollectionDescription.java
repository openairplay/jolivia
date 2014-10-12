package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.StringChunk;

@DMAPAnnotation(type=DmapChunkDefinition.aecp)
public class CollectionDescription extends StringChunk {

	public CollectionDescription() {
		this("");
	}

	public CollectionDescription(String string) {
		super("aecp","com.apple.itunes.collection-description",string);
	}

}
