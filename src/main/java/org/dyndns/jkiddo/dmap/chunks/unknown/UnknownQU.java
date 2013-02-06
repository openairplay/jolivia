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

import org.dyndns.jkiddo.dmap.chunks.UByteChunk;

public class UnknownQU extends UByteChunk
{
	public UnknownQU()
	{
		this(0);
	}

	public UnknownQU(int value)
	{
		super("ceQu", "com.apple.itunes.unknown-Qu", value);
	}

}