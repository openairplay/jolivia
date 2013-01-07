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

import org.ardverk.daap.chunks.impl.dmap.ContentCodesName;
import org.ardverk.daap.chunks.impl.dmap.ContentCodesNumber;
import org.ardverk.daap.chunks.impl.dmap.ContentCodesType;
import org.ardverk.daap.chunks.impl.dmap.Dictionary;

/**
 * A content code is essentially a description of a chunk.
 * 
 * @author Roger Kapsi
 */
public final class ContentCode extends Dictionary
{

	public ContentCode(int type, String name, int value)
	{
		super();

		add(new ContentCodesNumber(type));
		add(new ContentCodesName(name));
		add(new ContentCodesType(value));
	}
}
