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

package org.dyndns.jkiddo.dmap.chunks.dmap;

import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.ContainerChunk;

/**
 * The name is a bit misleading, it is NOT a java.util.Dictionary and it has nothing to do with the java.util.Dictionary data structure. This class is used to create a list of {@see de.kapsi.net.daap.chunks.ContentCode} capabilities (see Library for more information) of a DAAP server and it describes essentially all the other Chunks in this package (their contentCode, name and type).
 * 
 * @author Roger Kapsi
 */
public class Dictionary extends ContainerChunk
{

	public Dictionary()
	{
		super("mdcl", "dmap.dictionary");
	}

	public <T extends Chunk> T getSpecificChunk(Class<T> clazz)
	{
		return getSingleChunk(clazz);
	}
}
