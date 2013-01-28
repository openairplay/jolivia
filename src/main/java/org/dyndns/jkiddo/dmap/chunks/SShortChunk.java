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

package org.dyndns.jkiddo.dmap.chunks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A signed short
 */
public abstract class SShortChunk extends AbstractChunk implements ShortChunk
{

	private static final Logger LOG = LoggerFactory.getLogger(SShortChunk.class);

	public static final int MIN_VALUE = Short.MIN_VALUE;
	public static final int MAX_VALUE = Short.MAX_VALUE;

	protected int value = 0;

	public SShortChunk(int type, String name, int value)
	{
		super(type, name);
		setValue(value);
	}

	public SShortChunk(String type, String name, int value)
	{
		super(type, name);
		setValue(value);
	}

	@Override
	public int getValue()
	{
		return value;
	}

	@Override
	public void setValue(int value)
	{
		this.value = checkSShortRange(value);
	}

	/**
	 * Checks if #MIN_VALUE <= value <= #MAX_VALUE and if not an IllegalArgumentException is thrown.
	 */
	public static int checkSShortRange(int value) throws IllegalArgumentException
	{
		if(value < MIN_VALUE || value > MAX_VALUE)
		{
			if(LOG.isErrorEnabled())
			{
				LOG.error("Value is outside of signed short range: " + value);
			}
		}
		return value;
	}

	/**
	 * Returns {@see #SHORT_TYPE}
	 */
	@Override
	public int getType()
	{
		return Chunk.SHORT_TYPE;
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + getContentCodeString() + "; short)=" + getValue();
	}
}
