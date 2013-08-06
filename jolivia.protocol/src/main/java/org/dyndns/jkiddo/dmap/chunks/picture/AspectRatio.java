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
		super("pasp", "dpap.aspectratio", AspectRatio);
	}
}
