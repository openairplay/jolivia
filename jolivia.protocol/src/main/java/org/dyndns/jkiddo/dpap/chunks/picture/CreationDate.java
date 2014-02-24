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

import org.dyndns.jkiddo.dmp.chunks.DateChunk;

/**
 * DPAP.CreationDate The creation date of the image.
 * 
 * @author Charles Ikeson
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.picd)
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
	public CreationDate(long millis)
	{
		super("picd", "dpap.creationdate", millis / 1000);
	}
}
