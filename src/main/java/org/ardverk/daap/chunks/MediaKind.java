/*******************************************************************************
 * Copyright (c) 2012 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - initial API and implementation
 ******************************************************************************/
package org.ardverk.daap.chunks;

public class MediaKind extends UByteChunk
{

	public MediaKind()
	{
		this(1);
	}

	public MediaKind(int mode)
	{
		super("aeMK", "com.apple.itunes.mediakind", mode);
	}
}
