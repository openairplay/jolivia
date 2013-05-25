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

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

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
