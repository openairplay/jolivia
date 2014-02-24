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

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

/**
 * You can use this class to assign a rating to a Song to indicate how much you like or dislike a Song. iTunes displays this rating as a set of stars.
 * 
 * @author Roger Kapsi
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.asur)
public class SongUserRating extends UByteChunk
{

	/**
	 * Constant field for zero stars.
	 */
	public static final int NONE = 0;

	/**
	 * Constant field for one star.
	 */
	public static final int ONE = 20;

	/**
	 * Constant field for two stars.
	 */
	public static final int TWO = 40;

	/**
	 * Constant field for three stars.
	 */
	public static final int THREE = 60;

	/**
	 * Constant field for four stars.
	 */
	public static final int FOUR = 80;

	/**
	 * Constant field for five stars.
	 */
	public static final int FIVE = 100;

	/**
	 * Creates a new SongUserRating with zero stars. Use {@see #setValue(int)} change this value.
	 */
	public SongUserRating()
	{
		this(NONE);
	}

	/**
	 * Creates a new SongUserRating with the assigned rating. You can change this value with {@see #setValue(int)}.
	 * 
	 * @param <code>rating</code> the rating
	 */
	public SongUserRating(int rating)
	{
		super("asur", "daap.songuserrating", rating);
	}
}
