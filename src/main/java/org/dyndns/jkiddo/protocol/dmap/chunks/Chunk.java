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

package org.dyndns.jkiddo.protocol.dmap.chunks;

/**
 * A Chuck is a tagged value (key/value pair). Chunks can either contain other Chunks or data of various types.
 * 
 * @author Roger Kapsi
 */
public interface Chunk
{

	// Note this is technically a char as known
	// from C. An 8 bit unsigned value.
	/** Type for unsigned byte Chunks */
	public static final int U_BYTE_TYPE = 1;

	/** Type for signed byte Chunks */
	public static final int BYTE_TYPE = 2;

	/** Length of byte Chunks */
	public static final int BYTE_LENGTH = 1;

	/** Type for unsigned short Chunks */
	public static final int U_SHORT_TYPE = 3;

	/** Type for signed short Chunks */
	public static final int SHORT_TYPE = 4;

	/** Length of short chunks */
	public static final int SHORT_LENGTH = 2;

	/** Type for unsigned int Chunks */
	public static final int U_INT_TYPE = 5;

	/** Type for signed int Chunks */
	public static final int INT_TYPE = 6;

	/** Length of int chunks */
	public static final int INT_LENGTH = 4;

	/** Type for unsigned long Chunks */
	public static final int U_LONG_TYPE = 7;

	/** Type for long Chunks */
	public static final int LONG_TYPE = 8;

	/** Length of long chunks */
	public static final int LONG_LENGTH = 8;

	/** Type for String Chunks (encoded as UTF-8) */
	public static final int STRING_TYPE = 9;

	/** Type for Date Chunks (Time in <u>seconds</u> since 1970) */
	public static final int DATE_TYPE = 10;

	/** Length of date chunks */
	public static final int DATE_LENGTH = 4;

	/**
	 * Type for Version Chunks (an int value split up into major, minor and patch level)
	 */
	public static final int VERSION_TYPE = 11;

	/** Length of version chunks */
	public static final int VERSION_LENGTH = 4;

	/** Type for Container Chunks. Chunks that contain other Chunks */
	public static final int CONTAINER_TYPE = 12;

	public static final int RAW_TYPE = 13;

	/** */
	public int getContentCode();

	/** */
	public String getContentCodeString();

	/** */
	public String getName();

	/**
	 * Returns the type of this Chunk. For example {@see #BOOLEAN_TYPE}.
	 */
	public int getType();
}
