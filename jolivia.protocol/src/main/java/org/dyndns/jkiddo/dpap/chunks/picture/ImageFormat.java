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

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

/**
 * DPAP.ImageFormat
 * 
 * @author Charles Ikeson
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.pfmt)
public class ImageFormat extends StringChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImageFormat()
	{
		this("");
	}

	/**
	 * The format of the image data. Usually the file extension of the file.
	 * 
	 * @param imageFormat
	 *            The format of the image data.
	 */
	public ImageFormat(String imageFormat)
	{
		super("pfmt", "dpap.imageformat", imageFormat);
	}
}
