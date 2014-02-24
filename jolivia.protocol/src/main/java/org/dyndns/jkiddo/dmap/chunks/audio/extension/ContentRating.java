package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeCR)
public class ContentRating extends StringChunk
{
	public ContentRating()
	{
		this("");
	}

	public ContentRating(String b)
	{
		super("aeCR", "com.apple.itunes.content-rating", b);
	}
}
