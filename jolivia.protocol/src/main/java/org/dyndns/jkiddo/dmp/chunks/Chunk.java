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

package org.dyndns.jkiddo.dmp.chunks;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapTypeDefinition;

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
	public static final int U_BYTE_TYPE = DmapTypeDefinition.U_BYTE_TYPE.getType();

	/** Type for signed byte Chunks */
	public static final int BYTE_TYPE = DmapTypeDefinition.BYTE_TYPE.getType();

	/** Length of byte Chunks */
	public static final int BYTE_LENGTH = DmapTypeDefinition.BYTE_TYPE.getLength();

	/** Type for unsigned short Chunks */
	public static final int U_SHORT_TYPE = DmapTypeDefinition.U_SHORT_TYPE.getType();

	/** Type for signed short Chunks */
	public static final int SHORT_TYPE = DmapTypeDefinition.SHORT_TYPE.getType();

	/** Length of short chunks */
	public static final int SHORT_LENGTH = DmapTypeDefinition.SHORT_TYPE.getLength();

	/** Type for unsigned int Chunks */
	public static final int U_INT_TYPE = DmapTypeDefinition.U_INT_TYPE.getType();

	/** Type for signed int Chunks */
	public static final int INT_TYPE = DmapTypeDefinition.INT_TYPE.getType();

	/** Length of int chunks */
	public static final int INT_LENGTH = DmapTypeDefinition.INT_TYPE.getLength();

	/** Type for unsigned long Chunks */
	public static final int U_LONG_TYPE = DmapTypeDefinition.U_LONG_TYPE.getType();

	/** Type for long Chunks */
	public static final int LONG_TYPE = DmapTypeDefinition.LONG_TYPE.getType();

	/** Length of long chunks */
	public static final int LONG_LENGTH = DmapTypeDefinition.LONG_TYPE.getLength();

	/** Type for String Chunks (encoded as UTF-8) */
	public static final int STRING_TYPE = DmapTypeDefinition.STRING_TYPE.getType();

	/** Type for Date Chunks (Time in <u>seconds</u> since 1970) */
	public static final int DATE_TYPE = DmapTypeDefinition.DATE_TYPE.getType();

	/** Length of date chunks */
	public static final int DATE_LENGTH = DmapTypeDefinition.DATE_TYPE.getLength();

	/**
	 * Type for Version Chunks (an int value split up into major, minor and patch level)
	 */
	public static final int VERSION_TYPE = DmapTypeDefinition.VERSION_TYPE.getType();

	/** Length of version chunks */
	public static final int VERSION_LENGTH = DmapTypeDefinition.VERSION_TYPE.getLength();

	/** Type for Container Chunks. Chunks that contain other Chunks */
	public static final int CONTAINER_TYPE = DmapTypeDefinition.CONTAINER_TYPE.getType();

	public static final int RAW_TYPE = DmapTypeDefinition.RAW_TYPE.getType();

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
