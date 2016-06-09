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

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapTypeDefinition;

public abstract class RawChunk extends AbstractChunk
{
	private byte[] value;

	protected RawChunk(final String contentCode, final String name)
	{
		super(contentCode, name);
	}

	protected RawChunk(final String contentCode, final String name, final byte[] array)
	{
		super(contentCode, name);
		setValue(array);
	}

	public void setValue(final byte[] array)
	{
		value = new byte[array.length];
		System.arraycopy(array, 0, value, 0, value.length);
	}
	
	public byte[] getValue()
	{
		return value;
	}

	@Override
	public DmapTypeDefinition getType()
	{
		return DmapTypeDefinition.RAW_TYPE;
	}

	@Override
	public String toString(final int indent)
	{
		StringBuilder val  = new StringBuilder("{");
		for(int i = 0; i < value.length - 1; i++)
		{
			val.append((int)value[i]).append(", ");
		}
		val.append((int)value[value.length - 1]).append("}");
		return indent(indent) + name + "(" + getContentCodeString() + "; raw)=" + val;
	}
	
	@Override
	public void setObjectValue(final Object object)
	{
		setValue((byte[]) object);
	}
}
