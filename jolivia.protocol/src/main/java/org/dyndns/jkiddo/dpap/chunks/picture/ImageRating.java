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
 * DPAP.ImageRating The rating of the image.
 * 
 * @author Charles Ikeson
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.prat)
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
