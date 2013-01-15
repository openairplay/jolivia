package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

/**
 * DPAP.ImageFileSize The file size of the image.
 * 
 * @author Charles Ikeson
 */
public class ImageFileSize extends UIntChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImageFileSize()
	{
		this(0);
	}

	/**
	 * Creates the parameter using the passed value.
	 * 
	 * @param filesize
	 *            The filesize of the image.
	 */
	public ImageFileSize(int filesize)
	{
		super("pifs", "dpap.imagefilesize", filesize);
	}
}
