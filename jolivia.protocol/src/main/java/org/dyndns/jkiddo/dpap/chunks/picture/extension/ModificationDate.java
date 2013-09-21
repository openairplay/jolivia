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
package org.dyndns.jkiddo.dpap.chunks.picture.extension;

import org.dyndns.jkiddo.dmp.chunks.DateChunk;

public class ModificationDate extends DateChunk
{
	public ModificationDate()
	{
		this(0l);
	}

	public ModificationDate(long millis)
	{
		super("pemd", "com.apple.itunes.photos.modification-date", millis / 1000);
	}
}
