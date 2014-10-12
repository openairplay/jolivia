package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeMX)
public class MovieInfoXml extends StringChunk
{
	public MovieInfoXml()
	{
		this("");
	}

	public MovieInfoXml(String b)
	{
		super("aeMX", "com.apple.itunes.movie-info-xml", b);
	}
}
