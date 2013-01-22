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
package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.ULongChunk;

public class UnknownRM extends ULongChunk
{

	public UnknownRM()
	{
		this(0);
	}

	public UnknownRM(long value)
	{
		super("aeRM", "com.apple.itunes.unknown-RM", value);
	}
}
