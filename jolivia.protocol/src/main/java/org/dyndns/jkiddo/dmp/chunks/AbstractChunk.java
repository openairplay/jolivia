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

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.util.DmapUtil;

/**
 * An abstract base class for Chunks.
 * 
 * @author Roger Kapsi
 */
public abstract class AbstractChunk implements Chunk
{

	protected final int contentCode;
	protected final String name;
	private final DMAPAnnotation annotation = this.getClass().getAnnotation(DMAPAnnotation.class);

	/**
*
*/
	protected AbstractChunk(final String contentCode, final String name)
	{
		if(contentCode.length() != 4)
		{
			throw new IllegalArgumentException("Content Code must be 4 chars");
		}

		this.contentCode = DmapUtil.toContentCodeNumber(contentCode);
		this.name = name;
	}

	/**
*
*/
	protected AbstractChunk(final int contentCode, final String name)
	{
		this.contentCode = contentCode;
		this.name = name;
	}
	
	protected AbstractChunk()
	{
		name = annotation.type().getLongname();
		if(annotation.type().hashCode() == -1)
		{
			contentCode = DmapUtil.toContentCodeNumber(annotation.type().getShortname());
		}
		else
		{
			contentCode = DmapUtil.toContentCodeNumber(annotation.type().getShortname());
		}
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
		return annotation.type().getShortname();
//		return DmapUtil.toContentCodeString(contentCode);
	}

	/**
	 * Returns the name of this Chunk
	 */
	@Override
	public String getName()
	{
		return annotation.type().getLongname();
//		return name;
	}

	@Override
	public String toString()
	{
		return toString(0);
	}

	public String toString(final int indent)
	{
		return indent(indent) + name + "('" + getContentCodeString() + "')";
	}

	protected static String indent(final int indent)
	{
		final StringBuffer buffer = new StringBuffer(indent);
		for(int i = 0; i < indent; i++)
		{
			buffer.append(' ');
		}
		return buffer.toString();
	}

	public abstract void setObjectValue(Object object);
}
