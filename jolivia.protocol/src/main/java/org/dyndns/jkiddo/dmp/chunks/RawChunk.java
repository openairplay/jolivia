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
package org.dyndns.jkiddo.dmp.chunks;

public abstract class RawChunk extends AbstractChunk
{
	private byte[] value;

	protected RawChunk(String contentCode, String name)
	{
		super(contentCode, name);
	}

	protected RawChunk(String contentCode, String name, byte[] array)
	{
		super(contentCode, name);
		setValue(array);
	}

	public void setValue(byte[] array)
	{
		value = new byte[array.length];
		System.arraycopy(array, 0, value, 0, value.length);
	}
	
	public byte[] getValue()
	{
		return value;
	}

	@Override
	public int getType()
	{
		return RAW_TYPE;
	}

	@Override
	public String toString(int indent)
	{
		String val  = "{";
		for(int i = 0; i < value.length - 1; i++)
		{
			val += "" + (int)value[i] + ", ";
		}
		val += "" + (int)value[value.length - 1] + "}";
		return indent(indent) + name + "(" + getContentCodeString() + "; raw)=" + val;
	}
	
	@Override
	public void setObjectValue(Object object)
	{
		setValue((byte[]) object);
	}
}
