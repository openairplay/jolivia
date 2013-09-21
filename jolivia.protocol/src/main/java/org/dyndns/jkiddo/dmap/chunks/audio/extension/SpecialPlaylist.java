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
package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

public class SpecialPlaylist extends UByteChunk
{
	// 1 podcast library
	// 2 iTunesDJ library
	// 4 Movies library
	// 5 TVShows library
	// 6 Music Library
	// 7 Books Library
	// 8 Purchased library
	// 9 PurchasedOnDeviceLibrary
	// 12 Genius library
	// 13 iTunesU library
	// 15 GeniusMixes library
	// 16 GenisMix library
	public SpecialPlaylist()
	{
		this(0);
	}

	public SpecialPlaylist(int mode)
	{
		super("aePS", "com.apple.itunes.special-playlist", mode);
	}
}
