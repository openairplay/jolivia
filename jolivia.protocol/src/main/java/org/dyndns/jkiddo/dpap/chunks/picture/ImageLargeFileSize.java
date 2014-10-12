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

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

/**
 * DPAP.ImageLargeFileSize The filesize of the image as a long. Note: Not sure how this is viable since the pfdt tag can only be as large as an integer allows?
 * 
 * @author Charles Ikeson
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.plsz)
public class ImageLargeFileSize extends ULongChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImageLargeFileSize()
	{
		this(0l);
	}

	/**
	 * Creates the parameter using the passed value.
	 * 
	 * @param filesize
	 *            The filesize of the image, as a long.
	 */
	public ImageLargeFileSize(long filesize)
	{
		super("plsz", "dpap.imagelargefilesize", filesize);
	}

}
