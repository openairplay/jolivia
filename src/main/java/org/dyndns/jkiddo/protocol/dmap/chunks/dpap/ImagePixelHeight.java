package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

/**
 * DPAP.ImagePixelHeight The height of the image in pixels.
 * 
 * @author Charles Ikeson
 */
public class ImagePixelHeight extends UIntChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImagePixelHeight()
	{
		this(0);
	}

	/**
	 * Constructor for the height parameter using the passed value.
	 * 
	 * @param height
	 *            The height of the image.
	 */
	public ImagePixelHeight(int height)
	{
		super("phgt", "dpap.imagepixelheight", height);
	}
}
