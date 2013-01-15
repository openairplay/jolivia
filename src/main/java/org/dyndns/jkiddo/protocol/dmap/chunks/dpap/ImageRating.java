package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

/**
 * DPAP.ImageRating The rating of the image.
 * 
 * @author Charles Ikeson
 */
public class ImageRating extends UIntChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImageRating()
	{
		this(0);
	}

	/**
	 * Creates the parameter using the passed parameters.
	 * 
	 * @param Rating
	 *            The rating of the image.
	 */
	public ImageRating(int Rating)
	{
		super("prat", "dpap.imagerating", Rating);
	}
}
