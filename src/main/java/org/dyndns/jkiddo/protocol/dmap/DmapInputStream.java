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

package org.dyndns.jkiddo.protocol.dmap;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.dyndns.jkiddo.protocol.dmap.chunks.ByteChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.Chunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.ChunkFactory;
import org.dyndns.jkiddo.protocol.dmap.chunks.ContainerChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.DateChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.IntChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.LongChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.RawChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.ShortChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.VersionChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.SongArtist;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ListingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DmapInputStream extends FilterInputStream
{

	private static final Logger logger = LoggerFactory.getLogger(DmapInputStream.class);

	private ChunkFactory factory = null;

	private final boolean specialCaseProtocolViolation;

	public DmapInputStream(InputStream in)
	{
		super(in);
		this.specialCaseProtocolViolation = false;
	}
	
	public DmapInputStream(InputStream in, boolean specialCaseProtocolViolation)
	{
		super(in);
		this.specialCaseProtocolViolation = specialCaseProtocolViolation;
	}

	@Override
	public int read() throws IOException
	{
		int b = super.read();
		if(b < 0)
		{
			throw new EOFException();
		}
		return b;
	}

	/*
	 * Re: skip(length-Chunk.XYZ_LENGTH); iTunes states in Content-Codes responses that Chunk X is of type Y and has hence the length Z. A Byte has for example the length 1. But in some cases iTunes uses a different length for Bytes! It's probably a bug in iTunes...
	 */

	private int read(int length) throws IOException
	{
		skip(length - Chunk.BYTE_LENGTH);
		return read();
	}

	private int readShort(int length) throws IOException
	{
		skip(length - Chunk.SHORT_LENGTH);
		return (read() << 8) | read();
	}

	private int readInt(int length) throws IOException
	{
		skip(length - Chunk.INT_LENGTH);
		int size = Chunk.INT_LENGTH;
		ByteBuffer buffer = ByteBuffer.allocate(size);
		for(int i = 0; i < size; i++)
		{
			buffer.put((byte) read());
		}
		buffer.position(0);
		return buffer.getInt();
	}

	private long readLong(int length) throws IOException
	{
		skip(length - Chunk.LONG_LENGTH);
		int size = Chunk.LONG_LENGTH;
		ByteBuffer buffer = ByteBuffer.allocate(size);
		for(int i = 0; i < size; i++)
		{
			buffer.put((byte) read());
		}
		buffer.position(0);
		return buffer.getLong();
	}

	private String readString(int length) throws IOException
	{
		if(length == 0)
		{
			return null;
		}

		byte[] b = new byte[length];
		read(b, 0, b.length);
		return new String(b, DmapUtil.UTF_8);
	}

	private int readContentCode() throws IOException
	{
		return readInt(Chunk.INT_LENGTH);
	}

	private int readLength() throws IOException
	{
		return readInt(Chunk.INT_LENGTH);
	}

	public Chunk getChunk() throws IOException
	{
		int contentCode = readContentCode();
		int length = readLength();

		if(factory == null)
		{
			factory = new ChunkFactory();
		}

		Chunk chunk = factory.newChunk(contentCode);
		
		if(specialCaseProtocolViolation)
		{
			if(chunk.getClass().equals(ListingItem.class))
			{
				chunk = new SongArtist();
			}
		}

		if(length > 0)
		{
			if(chunk instanceof ByteChunk)
			{
				checkLength(chunk, Chunk.BYTE_LENGTH, length);
				((ByteChunk) chunk).setValue(read(length));
			}
			else if(chunk instanceof ShortChunk)
			{
				checkLength(chunk, Chunk.SHORT_LENGTH, length);
				((ShortChunk) chunk).setValue(readShort(length));
			}
			else if(chunk instanceof IntChunk)
			{
				checkLength(chunk, Chunk.INT_LENGTH, length);
				((IntChunk) chunk).setValue(readInt(length));
			}
			else if(chunk instanceof LongChunk)
			{
				checkLength(chunk, Chunk.LONG_LENGTH, length);
				((LongChunk) chunk).setValue(readLong(length));
			}
			else if(chunk instanceof StringChunk)
			{
				((StringChunk) chunk).setValue(readString(length));
			}
			else if(chunk instanceof DateChunk)
			{
				checkLength(chunk, Chunk.DATE_LENGTH, length);
				((DateChunk) chunk).setValue(readInt(length));
			}
			else if(chunk instanceof VersionChunk)
			{
				checkLength(chunk, Chunk.VERSION_LENGTH, length);
				((VersionChunk) chunk).setValue(readInt(length));
			}
			else if(chunk instanceof RawChunk)
			{
				byte[] b = new byte[length];
				read(b, 0, b.length);
				((RawChunk) chunk).setValue(b);
			}
			else if(chunk instanceof ContainerChunk)
			{
				byte[] b = new byte[length];
				read(b, 0, b.length);
				DmapInputStream in = new DmapInputStream(new ByteArrayInputStream(b), this.specialCaseProtocolViolation);
				while(in.available() > 0)
				{
					((ContainerChunk) chunk).add(in.getChunk());
				}
				in.close();
			}
			else
			{
				throw new IOException("Unknown Chunk Type: " + chunk);
			}
		}

		return chunk;
	}

	/**
	 * Throws an IOE if expected differs from length
	 */
	private static void checkLength(Chunk chunk, int expected, int length)
	{
		if(expected != length)
		{
			// throw new IOException("Expected a chunk with length " + expected
			// + " but got " + length + " (" + chunk.getContentCodeString() +
			// ")");

			if(logger.isWarnEnabled())
			{
				logger.warn("Expected a chunk with length " + expected + " but got " + length + " (" + chunk.getContentCodeString() + ")");
			}
		}
	}
}
