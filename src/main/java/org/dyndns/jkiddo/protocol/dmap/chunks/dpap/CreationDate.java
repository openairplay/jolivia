package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.DateChunk;

/**
 * DPAP.CreationDate The creation date of the image.
 * 
 * @author Charles Ikeson
 */
public class CreationDate extends DateChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public CreationDate()
	{
		this(0l);
	}

	/**
	 * Creates the parameter using the passed value.
	 * 
	 * @param CreationDate
	 *            The creation date of the image.
	 */
	public CreationDate(long seconds)
	{
		super("picd", "dpap.creationdate", seconds);
	}
}
