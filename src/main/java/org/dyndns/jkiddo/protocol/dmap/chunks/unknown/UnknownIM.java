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

public class UnknownIM extends ULongChunk
{
	public UnknownIM()
	{
		this(0);
	}

	public UnknownIM(long value)
	{
		super("aeIM", "com.apple.itunes.unknown-IM", value);
	}
}
