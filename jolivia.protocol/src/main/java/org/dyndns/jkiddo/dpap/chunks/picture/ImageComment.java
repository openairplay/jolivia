package org.dyndns.jkiddo.dpap.chunks.picture;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.pcmt)
public class ImageComment extends StringChunk
{
	public ImageComment()
	{
		this("");
	}

	public ImageComment(String comment)
	{
		super("pcmt", "dpap.imagecomments", comment);
	}
}
