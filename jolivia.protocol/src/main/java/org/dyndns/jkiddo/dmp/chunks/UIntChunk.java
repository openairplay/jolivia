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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An unsigned int
 */
public abstract class UIntChunk extends AbstractChunk implements IntChunk
{

	private static final Logger LOGGER = LoggerFactory.getLogger(UIntChunk.class);

	public static final long MIN_VALUE = 0l;
	public static final long MAX_VALUE = 0xFFFFFFFFl;

	protected int value;

	public UIntChunk(final String type, final String name, final long value)
	{
		super(type, name);
		setValue(value);
	}

	public UIntChunk(final int type, final String name, final int hasChilds)
	{
		super(type, name);
		setValue(value);
	}

	@Override
	public void setValue(final int value)
	{
		this.value = value;
	}

	public void setValue(final long value)
	{
		setValue((int) checkUIntRange(value));
	}

	@Override
	public int getValue()
	{
		return value;
	}

	public long getUnsignedValue()
	{
		return getValue() & MAX_VALUE;
	}

	/**
	 * Checks if #MIN_VALUE <= value <= #MAX_VALUE and if not an IllegalArgumentException is thrown.
	 */
	public static long checkUIntRange(final long value) throws IllegalArgumentException
	{
		if(value < MIN_VALUE || value > MAX_VALUE)
		{
			LOGGER.error("Value is outside of unsigned int range: " + value);
		}
		return value;
	}

	/**
	 * Returns {@see #U_INT_TYPE}
	 */
	@Override
	public DmapTypeDefinition getType()
	{
		return DmapTypeDefinition.U_INT_TYPE;
	}

	@Override
	public String toString(final int indent)
	{
		return indent(indent) + name + "(" + getContentCodeString() + "; uint)=" + getUnsignedValue();
	}
	
	@Override
	public void setObjectValue(final Object object)
	{
		setValue((Integer) object);
	}
}
