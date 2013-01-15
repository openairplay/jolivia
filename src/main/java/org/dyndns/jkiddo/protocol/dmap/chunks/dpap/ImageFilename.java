package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

/**
 * DPAP.ImageFilename The filename of the image.
 * 
 * @author Charles Ikeson
 */
public class ImageFilename extends StringChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImageFilename()
	{
		this("");
	}

	/**
	 * Creates the parameter using the passed value.
	 * 
	 * @param filename
	 *            The filename of the image.
	 */
	public ImageFilename(String filename)
	{
		super("pimf", "dpap.imagefilename", filename);
	}

}
