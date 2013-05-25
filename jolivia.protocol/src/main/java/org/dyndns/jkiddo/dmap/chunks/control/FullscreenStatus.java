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

import org.dyndns.jkiddo.dmap.chunks.UByteChunk;

public class FullscreenStatus extends UByteChunk
{
	public FullscreenStatus()
	{
		this(0);
	}

	public FullscreenStatus(int i)
	{
		super("cafs", "com.apple.itunes.unknown-fs", i);
	}
}
