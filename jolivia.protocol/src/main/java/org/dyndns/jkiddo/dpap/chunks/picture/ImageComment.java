package org.dyndns.jkiddo.dpap.chunks.picture;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

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
