/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
package org.dyndns.jkiddo.dmap.chunks.picture;

import org.dyndns.jkiddo.dmap.chunks.RawChunk;

/**
 * DPAP.FileDate This tag holds the raw file data of a picture or thumb nail. The data that is stored depends on what is requested by the client. This differs from the DAAP protocol where the file is requested separately from the tag.
 * 
 * @author Charles Ikeson
 */
public class FileData extends RawChunk
{

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public FileData()
	{
		this(new byte[] {});
	}

	/**
	 * Creates the parameter and points it to the original file.
	 * 
	 * @param f
	 *            The file to read from.
	 * @param asThumb
	 *            Resizes the image to a max of 240x240 before sending it back.
	 */

	public FileData(byte[] array)
	{
		// dpap.thumb
		// dpap.hires
		super("pfdt", "dpap.filedata", array);
	}
}
