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

/**
 * A signed int
 */
public abstract class SIntChunk extends AbstractChunk implements IntChunk
{

	public static final int MIN_VALUE = Integer.MIN_VALUE;
	public static final int MAX_VALUE = Integer.MAX_VALUE;

	protected int value = 0;

	public SIntChunk(int type, String name, int value)
	{
		super(type, name);
		setValue(value);
	}

	public SIntChunk(String type, String name, int value)
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
		this.value = value;
	}

	/**
	 * Returns {@see #INT_TYPE}
	 */
	@Override
	public int getType()
	{
		return Chunk.INT_TYPE;
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + getContentCodeString() + "; int)=" + getValue();
	}
}
