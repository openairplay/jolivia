package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

/**
 * DPAP.ImagePixelWidth The width of the image in pixels.
 * 
 * @author Charles Ikeson
 */
public class ImagePixelWidth extends UIntChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImagePixelWidth()
	{
		this(0);
	}

	/**
	 * Constructor for the height parameter using the passed value.
	 * 
	 * @param width
	 *            The width of the image.
	 */
	public ImagePixelWidth(int width)
	{
		super("pwth", "dpap.imagepixelwidth", width);
	}
}
