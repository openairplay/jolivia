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

import java.nio.ByteBuffer;
import java.util.Map;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.ProtocolViolationException;
import org.dyndns.jkiddo.dmp.util.DmapUtil;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public final class ChunkFactory
{
	private static final ImmutableMap<Integer, DMAPAnnotation> calculatedMap;

	static
	{
		final Builder<Integer, DMAPAnnotation> table = new ImmutableMap.Builder<Integer, DMAPAnnotation>();
		for( final DmapChunkDefinition enumDefinitions : IDmapProtocolDefinition.DmapChunkDefinition.values())
		{
			final DMAPAnnotation dmapAnnotation = enumDefinitions.getClazz().getAnnotation(DMAPAnnotation.class);
			if(dmapAnnotation == null)
			{
				throw new RuntimeException("No matching annotation found for class: '" + enumDefinitions.getClazz() + "'");
			}

			int shortNameAsInt = dmapAnnotation.hash();
			if(shortNameAsInt == -1)
				shortNameAsInt = DmapUtil.toContentCodeNumber(dmapAnnotation.type().getShortname());

			table.put(shortNameAsInt, dmapAnnotation);
			
		}
		calculatedMap = table.build();
	}
	
	public ChunkFactory() {
	}

	public Map<Integer, DMAPAnnotation> getCalculatedMap()
	{
		return calculatedMap;
	}
	
	private Class<? extends Chunk> getChunkClass(final int contentCode) throws ProtocolViolationException
	{
		try
		{
			return calculatedMap.get(new Integer(contentCode)).type().getClazz();
		}
		catch(final Exception err)
		{
			throw new ProtocolViolationException("Content code: " + contentCode + " not found. Hash is 0x" + contentCode, err);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Chunk> T newChunk(final String contentCode) throws ProtocolViolationException
	{
		final Class<? extends Chunk> clazz = getChunkClass(stringReadAsInt(contentCode));
		try {
			return (T) clazz.newInstance();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static int stringReadAsInt(final String s)
	{
		final ByteBuffer buffer = ByteBuffer.allocate(s.length());
		for(int i = 0; i < s.length(); i++)
		{
			buffer.put((byte) s.charAt(i));
		}
		buffer.position(0);
		return buffer.getInt();
	}
}
