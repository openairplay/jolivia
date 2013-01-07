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

package org.ardverk.daap.chunks.impl.dmap;

import org.ardverk.daap.DaapUtil;
import org.ardverk.daap.chunks.UIntChunk;

/**
 * The number of a ContentCode (it's actually a four character code). It is needed to build a list of capabilities of the Server which is send to the client...
 * 
 * @author Roger Kapsi
 */
public class ContentCodesNumber extends UIntChunk
{

	public ContentCodesNumber()
	{
		this(0);
	}

	public ContentCodesNumber(long number)
	{
		super("mcnm", "dmap.contentcodesnumber", number);
	}

	public String getValueContentCode()
	{
		return DaapUtil.toContentCodeString(getValue());
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + contentCode + "; int)=" + getValueContentCode();
	}
}
