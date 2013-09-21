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

import java.math.BigInteger;

/**
 * An unsigned long
 */
public abstract class ULongChunk extends AbstractChunk implements LongChunk
{

	public static final String MIN_VALUE = "0";
	public static final String MAX_VALUE = "18446744073709551615";

	protected long value = 0;

	public ULongChunk(int type, String name, long value)
	{
		super(type, name);
		setValue(value);
	}

	public ULongChunk(String type, String name, long value)
	{
		super(type, name);
		setValue(value);
	}

	@Override
	public void setValue(long value)
	{
		this.value = value;
	}

	@Override
	public long getValue()
	{
		return value;
	}

	public BigInteger getUnsignedValue()
	{
		long l = getValue();
		byte[] b = new byte[8 + 1];

		b[0] = 0;
		b[1] = (byte) ((l >> 56l) & 0xFF);
		b[2] = (byte) ((l >> 48l) & 0xFF);
		b[3] = (byte) ((l >> 40l) & 0xFF);
		b[4] = (byte) ((l >> 32l) & 0xFF);
		b[5] = (byte) ((l >> 24l) & 0xFF);
		b[6] = (byte) ((l >> 16l) & 0xFF);
		b[7] = (byte) ((l >> 8l) & 0xFF);
		b[8] = (byte) ((l) & 0xFF);

		return new BigInteger(b);
	}

	/**
	 * Returns {@see #U_LONG_TYPE}
	 */
	@Override
	public int getType()
	{
		return Chunk.U_LONG_TYPE;
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + getContentCodeString() + "; ulong)=" + getUnsignedValue();
	}
}
