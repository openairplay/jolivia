package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

/**
 * DPAP.ImageFormat
 * 
 * @author Charles Ikeson
 */
public class ImageFormat extends StringChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImageFormat()
	{
		this("");
	}

	/**
	 * The format of the image data. Usually the file extension of the file.
	 * 
	 * @param imageFormat
	 *            The format of the image data.
	 */
	public ImageFormat(String imageFormat)
	{
		super("pfmt", "dpap.imageformat", imageFormat);
	}
}
