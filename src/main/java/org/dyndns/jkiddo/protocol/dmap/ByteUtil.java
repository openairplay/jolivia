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

package org.dyndns.jkiddo.protocol.dmap;

/**
 * Miscellaneous Java primitive to byte array and vice versa methods.
 * <p>
 * Note: All values are in Big-Endian!
 * </p>
 * 
 * @author Roger Kapsi
 */
public final class ByteUtil
{

	private ByteUtil()
	{}

	/**
	 * 16bit to int
	 */
	public static final int toInt16BE(byte[] src, int offset)
	{
		return(((src[offset] & 0xFF) << 8) + (src[++offset] & 0xFF));
	}

	/**
	 * int to 16bit
	 */
	public static final int toByte16BE(int value, byte[] dst, int offset)
	{
		dst[offset] = (byte) ((value >> 8) & 0xFF);
		dst[++offset] = (byte) (value & 0xFF);
		return 2;
	}

	/**
	 * 32bit to int
	 */
	public static final int toIntBE(byte[] src, int offset)
	{
		return(((src[offset] & 0xFF) << 24) + ((src[++offset] & 0xFF) << 16) + ((src[++offset] & 0xFF) << 8) + (src[++offset] & 0xFF));
	}

	/**
	 * Copies the first 4 characters of fourChars to dst
	 */
	public static final int toFourCharBytes(String fourChars, byte[] dst, int offset) throws IllegalArgumentException
	{

		final int length = fourChars.length();

		if(length > 4)
			throw new IllegalArgumentException("Illegal fourChars length: " + length);

		for(int i = 0; i < length; i++)
		{
			dst[offset++] = (byte) (fourChars.charAt(i) & 0xFF);
		}

		return 4;
	}

	/**
	 * This method converts the first 4 characters of fourChars to an integer.
	 */
	public static final int toFourCharCode(String fourChars) throws IllegalArgumentException
	{

		final byte[] dst = new byte[4];
		toFourCharBytes(fourChars, dst, 0);
		return toIntBE(dst, 0);
	}

	/**
	 * int to 32bit
	 */
	public static final int toByteBE(int value, byte[] dst, int offset)
	{
		dst[offset] = (byte) ((value >> 24) & 0xFF);
		dst[++offset] = (byte) ((value >> 16) & 0xFF);
		dst[++offset] = (byte) ((value >> 8) & 0xFF);
		dst[++offset] = (byte) (value & 0xFF);
		return 4;
	}

	/**
	 * long to 64bit
	 */
	public static final int toByte64BE(long value, byte[] dst, int offset)
	{
		dst[offset] = (byte) ((value >> 56) & 0xFF);
		dst[++offset] = (byte) ((value >> 48) & 0xFF);
		dst[++offset] = (byte) ((value >> 40) & 0xFF);
		dst[++offset] = (byte) ((value >> 32) & 0xFF);
		dst[++offset] = (byte) ((value >> 24) & 0xFF);
		dst[++offset] = (byte) ((value >> 16) & 0xFF);
		dst[++offset] = (byte) ((value >> 8) & 0xFF);
		dst[++offset] = (byte) (value & 0xFF);
		return 8;
	}

	/**
	 * 64bit to long
	 */
	public static final long toLongBE(byte[] src, int offset)
	{
		return(((src[offset] & 0xFFl) << 56l) + ((src[++offset] & 0xFFl) << 48l) + ((src[++offset] & 0xFFl) << 40l) + ((src[++offset] & 0xFFl) << 32l) + ((src[++offset] & 0xFFl) << 24l) + ((src[++offset] & 0xFFl) << 16l) + ((src[++offset] & 0xFFl) << 8l) + (src[++offset] & 0xFFl));
	}
}
