package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.agar)
public class ArtistSearchContainer extends ContainerChunk {
	public ArtistSearchContainer() {
		super("agar","unknown.ar");
	}
}
