package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.ULongChunk;

/**
 * DPAP.ImageLargeFileSize The filesize of the image as a long. Note: Not sure how this is viable since the pfdt tag can only be as large as an integer allows?
 * 
 * @author Charles Ikeson
 */
public class ImageLargeFileSize extends ULongChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImageLargeFileSize()
	{
		this(0l);
	}

	/**
	 * Creates the parameter using the passed value.
	 * 
	 * @param filesize
	 *            The filesize of the image, as a long.
	 */
	public ImageLargeFileSize(long filesize)
	{
		super("plsz", "dpap.imagelargefilesize", filesize);
	}

}
