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
package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.UShortChunk;

public class UnknownHC extends UShortChunk
{
	public UnknownHC()
	{
		this(0);
	}

	public UnknownHC(int value)
	{
		super("mshc", "com.apple.itunes.unknown-hl", value);
	}
}
