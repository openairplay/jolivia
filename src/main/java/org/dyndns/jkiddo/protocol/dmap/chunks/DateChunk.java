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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an implementation of a date chunk. The date is an integer int seconds since 1.1.1970.
 * 
 * @author Roger Kapsi
 */
public abstract class DateChunk extends AbstractChunk
{

	private static final Logger LOG = LoggerFactory.getLogger(DateChunk.class);

	public static final long MIN_VALUE = 0l;
	public static final long MAX_VALUE = 0xFFFFFFFFl;

	protected int date;

	public DateChunk(int type, String name, long value)
	{
		super(type, name);
		setValue(value);
	}

	public DateChunk(String type, String name, long date)
	{
		super(type, name);
		setValue(date);
	}

	public long getValue()
	{
		return date & MAX_VALUE;
	}

	public void setValue(long date)
	{
		this.date = (int) checkDateRange(date);
	}

	/**
	 * Checks if #MIN_VALUE <= value <= #MAX_VALUE and if not an IllegalArgumentException is thrown.
	 */
	public static long checkDateRange(long value) throws IllegalArgumentException
	{
		if(value < MIN_VALUE || value > MAX_VALUE)
		{
			if(LOG.isErrorEnabled())
			{
				LOG.error("Value is outside of Date range: " + value);
			}
		}
		return value;
	}

	/**
	 * Returns {@see #DATE_TYPE}
	 */
	@Override
	public int getType()
	{
		return Chunk.DATE_TYPE;
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + getContentCodeString() + "; date)=" + getValue();
	}
}
