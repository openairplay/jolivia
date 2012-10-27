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

import org.ardverk.daap.DaapUtil;

/**
 * An abstract base class for Chunks.
 * 
 * @author Roger Kapsi
 */
public abstract class AbstractChunk implements Chunk
{

	protected final int contentCode;
	protected final String name;

	/**
*
*/
	protected AbstractChunk(String contentCode, String name)
	{
		if(contentCode.length() != 4)
		{
			throw new IllegalArgumentException("Content Code must be 4 chars");
		}

		this.contentCode = DaapUtil.toContentCodeNumber(contentCode);
		this.name = name;
	}

	/**
*
*/
	protected AbstractChunk(int contentCode, String name)
	{
		this.contentCode = contentCode;
		this.name = name;
	}

	@Override
	public int getContentCode()
	{
		return contentCode;
	}

	/**
	 * Returns the 4 charecter content code of this Chunk as String
	 */
	@Override
	public String getContentCodeString()
	{
		return DaapUtil.toContentCodeString(contentCode);
	}

	/**
	 * Returns the name of this Chunk
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the type of this Chunk.
	 */
	@Override
	public abstract int getType();

	@Override
	public String toString()
	{
		return toString(0);
	}

	public String toString(int indent)
	{
		return indent(indent) + name + "('" + getContentCodeString() + "')";
	}

	protected static String indent(int indent)
	{
		StringBuffer buffer = new StringBuffer(indent);
		for(int i = 0; i < indent; i++)
		{
			buffer.append(' ');
		}
		return buffer.toString();
	}
}
