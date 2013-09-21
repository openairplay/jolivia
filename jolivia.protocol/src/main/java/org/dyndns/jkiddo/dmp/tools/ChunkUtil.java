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

package org.dyndns.jkiddo.dmp.tools;

import java.io.File;
import java.io.FilenameFilter;

import org.dyndns.jkiddo.dmp.chunks.AbstractChunk;
import org.dyndns.jkiddo.dmp.chunks.Chunk;

public final class ChunkUtil
{

	public static final String CHUNK_PACKAGE = Chunk.class.getPackage().getName();
	public static final String CHUNK_IMPL_PACKAGE = CHUNK_PACKAGE + ".impl";

	public static final String CHUNK_DIR = CHUNK_PACKAGE.replace('.', File.separatorChar);
	public static final String CHUNK_IMPL_DIR = CHUNK_IMPL_PACKAGE.replace('.', File.separatorChar);

	// private static final Map map = new HashMap();

	static
	{

	}

	private ChunkUtil()
	{

	}

	public static Chunk[] getChunks()
	{
		return getChunks(new File(CHUNK_IMPL_DIR));
	}

	public static Chunk[] getChunks(File dir)
	{
		dir = new File("C:/Users/JKidd/workspace/jolivia/src/main/java/" + dir.toString());
		String[] files = dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".java");
			}
		});

		Chunk[] chunks = new Chunk[files.length];

		try
		{
			for(int i = 0; i < files.length; i++)
			{
				String clazzName = CHUNK_IMPL_PACKAGE + "." + files[i].substring(0, files[i].length() - ".java".length());
				Class<?> clazz = Class.forName(clazzName);

				chunks[i] = (AbstractChunk) clazz.newInstance();
			}
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch(InstantiationException e)
		{
			throw new RuntimeException(e);
		}
		catch(IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		return chunks;
	}
}
