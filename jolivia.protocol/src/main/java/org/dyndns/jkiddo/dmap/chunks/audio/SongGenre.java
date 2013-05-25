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
/*
 * Digital Audio Access Protocol (DAAP) Library
 * Copyright (C) 2004-2010 Roger Kapsi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

/**
 * The genre of the Song. You can either use the pre-defined genres like ALTERNATIVE, JAZZ etc. or you can use your custom genres.
 * 
 * @author Roger Kapsi
 */
public class SongGenre extends StringChunk
{

	/** No genre */
	public static final String NONE = null;

	/** Alternative */
	public static final String ALTERNATIVE = "Alternative";

	/** Blues/R&amp;B */
	public static final String BLUES_RB = "Blues/R&B";

	/** Books &amp; Spoken */
	public static final String BOOKS_SPOKEN = "Books & Spoken";

	/** Children's Music */
	public static final String CHILDRENS_MUSIC = "Children's Music";

	/** Classical */
	public static final String CLASSICAL = "Classical";

	/** Country */
	public static final String COUNTRY = "Country";

	/** Dance */
	public static final String DANCE = "Dance";

	/** Easy Listening */
	public static final String EASY_LISTENING = "Easy Listening";

	/** Electronic */
	public static final String ELECTRONIC = "Electronic";

	/** Folk */
	public static final String FOLK = "Folk";

	/** Hip Hop/Rap */
	public static final String HIP_HOP_RAP = "Hip Hop/Rap";

	/** Holiday */
	public static final String HOLIDAY = "Holiday";

	/** House */
	public static final String HOUSE = "House";

	/** Industrial */
	public static final String INDUSTRIAL = "Industrial";

	/** Jazz */
	public static final String JAZZ = "Jazz";

	/** New Age */
	public static final String NEW_AGE = "New Age";

	/** Pop */
	public static final String POP = "Pop";

	/** Religious */
	public static final String RELIGIOUS = "Religious";

	/** Rock */
	public static final String ROCK = "Rock";

	/** Soundtrack */
	public static final String SOUNDTRACK = "Soundtrack";

	/** Techno */
	public static final String TECHNO = "Techno";

	/** Trance */
	public static final String TRANCE = "Trance";

	/** Unclassifiable */
	public static final String UNCLASSIFIABLE = "Unclassifiable";

	/** World */
	public static final String WORLD = "World";

	/**
	 * Creates a new SongGenre where genre is not set. You can change this value with {@see #setValue(String)}.
	 */
	public SongGenre()
	{
		this(NONE);
	}

	/**
	 * Creates a new SongGenre with the assigned genre. You can change this value with {@see #setValue(String)}.
	 * 
	 * @param <tt>genre</tt> the genre of this song or <tt>null</tt> if no genre is set.
	 */
	public SongGenre(String genre)
	{
		super("asgn", "daap.songgenre", genre);
	}
}
