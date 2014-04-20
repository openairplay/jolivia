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

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type = DmapProtocolDefinition.aePS)
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
	// 10 Rentals
	// 12 Genius library
	// 13 iTunesU library
	// 15 GeniusMixes library
	// 16 GenisMix library

	public static final int PODCAST_LIBRARY = 1;
	public static final int ITUNES_DJ_LIBRARY = 2;
	public static final int MOVIES_LIBRARY = 4;
	public static final int TV_SHOWS__LIBRARY = 5;
	public static final int MUSIC_LIBRARY = 6;
	public static final int BOOKS_LIBRARY = 7;
	public static final int PRUCHASED_LIBRARY = 8;
	public static final int PURCHASED_ON_DEVICE_LIBRARY = 9;
	public static final int RENTALS_LIBRARY = 10;
	public static final int GENIUS_LIBRARY = 12;
	public static final int iTUNES_U_LIBRARY = 13;
	public static final int GENIUS_MIXES_LIBRARY = 15;
	public static final int GENIUS_MIX_LIBRARY = 16;

	public SpecialPlaylist()
	{
		this(0);
	}

	public SpecialPlaylist(int mode)
	{
		super("aePS", "com.apple.itunes.special-playlist", mode);
	}
}
