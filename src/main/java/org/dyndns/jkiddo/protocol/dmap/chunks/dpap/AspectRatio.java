package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

/**
 * DPAP.AspectRatio parameter Stores the aspect ratio as a float converted to a string.
 * 
 * @author Charles Ikeson
 */
public class AspectRatio extends StringChunk
{

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public AspectRatio()
	{
		this("");
	}

	/**
	 * Creates the parameter and sets it to the passed value.
	 * 
	 * @param AspectRatio
	 *            The aspect ratio to set the tag to.
	 */
	public AspectRatio(String AspectRatio)
	{
		super("pasp", "dpad.aspectratio", AspectRatio);
	}
}
