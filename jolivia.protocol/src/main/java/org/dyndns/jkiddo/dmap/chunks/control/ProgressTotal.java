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
package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class ProgressTotal extends UIntChunk
{
	public ProgressTotal()
	{
		this(0);
	}

	public ProgressTotal(int value)
	{
		// # total track length in ms
		super("cast", "dacp.songtime", value);
	}

}
