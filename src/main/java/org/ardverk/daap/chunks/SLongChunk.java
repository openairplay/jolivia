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

package org.ardverk.daap.chunks;

/**
 * A signed long
 */
public abstract class SLongChunk extends AbstractChunk implements LongChunk
{

	public static final long MIN_VALUE = Long.MIN_VALUE;
	public static final long MAX_VALUE = Long.MAX_VALUE;

	protected long value = 0;

	public SLongChunk(int type, String name, long value)
	{
		super(type, name);
		setValue(value);
	}

	public SLongChunk(String type, String name, long value)
	{
		super(type, name);
		setValue(value);
	}

	@Override
	public long getValue()
	{
		return value;
	}

	@Override
	public void setValue(long value)
	{
		this.value = value;
	}

	/**
	 * Returns {@see #LONG_TYPE}
	 */
	@Override
	public int getType()
	{
		return Chunk.LONG_TYPE;
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + getContentCodeString() + "; long)=" + getValue();
	}
}
