package org.dyndns.jkiddo.dmap.chunks.picture;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

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
