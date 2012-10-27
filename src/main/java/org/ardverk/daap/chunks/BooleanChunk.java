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
 * An implementation of a boolean chunk.
 * 
 * @author Roger Kapsi
 */
public abstract class BooleanChunk extends UByteChunk
{

	public BooleanChunk(int type, String name, boolean value)
	{
		super(type, name, (value ? 1 : 0));
	}

	public BooleanChunk(String type, String name, boolean value)
	{
		super(type, name, (value ? 1 : 0));
	}

	public boolean getBooleanValue()
	{
		return getValue() != 0;
	}

	public void setValue(boolean value)
	{
		super.setValue(value ? 1 : 0);
	}

	@Override
	public void setValue(int value)
	{
		// normalize to 1 and 0 for easier debugging
		super.setValue((value != 0) ? 1 : 0);
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + getContentCodeString() + "; boolean)=" + getBooleanValue();
	}
}
