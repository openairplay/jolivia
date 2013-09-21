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
package org.dyndns.jkiddo.dpap.chunks.picture;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

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
