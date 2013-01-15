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

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.dyndns.jkiddo.protocol.dmap.chunks.BooleanChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.ByteChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.Chunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.ContainerChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.DateChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.IntChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.LongChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.ShortChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.VersionChunk;

public class DmapOutputStream extends FilterOutputStream
{

	public DmapOutputStream(OutputStream out)
	{
		super(out);
	}

	public void writeShort(int i) throws IOException
	{
		write((i >> 8) & 0xFF);
		write(i & 0xFF);
	}

	public void writeInt(int i) throws IOException
	{
		write((i >> 24) & 0xFF);
		write((i >> 16) & 0xFF);
		write((i >> 8) & 0xFF);
		write(i & 0xFF);
	}

	public void writeLong(long l) throws IOException
	{
		write((int) ((l >> 56l) & 0xFF));
		write((int) ((l >> 48l) & 0xFF));
		write((int) ((l >> 40l) & 0xFF));
		write((int) ((l >> 32l) & 0xFF));
		write((int) ((l >> 24l) & 0xFF));
		write((int) ((l >> 16l) & 0xFF));
		write((int) ((l >> 8l) & 0xFF));
		write((int) (l & 0xFF));
	}

	/*
	 * public void writeString(String s) throws IOException { if (s != null && s.length() > 0) { write(s.getBytes(DaapUtil.UTF_8)); } }
	 */

	public void writeContentCode(int contentCode) throws IOException
	{
		writeInt(contentCode);
	}

	public void writeLength(int length) throws IOException
	{
		writeInt(length);
	}

	public void writeByteChunk(ByteChunk chunk) throws IOException
	{
		int value = chunk.getValue();

		// Don't write booleans if state is false b/c
		// the presence of the chunk is interpereted
		// as true or false (i.e. value is don't care)!
		if(value == 0 && chunk instanceof BooleanChunk)
		{
			return;
		}

		writeContentCode(chunk.getContentCode());
		writeLength(1);
		write(value);
	}

	public void writeShortChunk(ShortChunk chunk) throws IOException
	{
		int value = chunk.getValue();

		writeContentCode(chunk.getContentCode());
		writeLength(2);
		writeShort(value);
	}

	public void writeIntChunk(IntChunk chunk) throws IOException
	{
		int value = chunk.getValue();

		writeContentCode(chunk.getContentCode());
		writeLength(4);
		writeInt(value);
	}

	public void writeLongChunk(LongChunk chunk) throws IOException
	{
		long value = chunk.getValue();

		writeContentCode(chunk.getContentCode());
		writeLength(8);
		writeLong(value);
	}

	public void writeStringChunk(StringChunk chunk) throws IOException
	{
		byte[] value = chunk.getBytes();

		writeContentCode(chunk.getContentCode());
		writeLength(value.length);
		write(value, 0, value.length);
	}

	public void writeDateChunk(DateChunk chunk) throws IOException
	{
		int value = (int) (chunk.getValue() & 0xFFFFFFFF);

		writeContentCode(chunk.getContentCode());
		writeLength(4);
		writeInt(value);
	}

	public void writeVersionChunk(VersionChunk chunk) throws IOException
	{
		int value = (int) (chunk.getValue() & 0xFFFFFFFF);

		writeContentCode(chunk.getContentCode());
		writeLength(4);
		writeInt(value);
	}

	public void writeContainerChunk(ContainerChunk chunk) throws IOException
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DmapOutputStream out = new DmapOutputStream(buffer);
		for(Chunk chnk : chunk)
			out.writeChunk(chnk);
		out.close();
		byte[] b = buffer.toByteArray();

		writeContentCode(chunk.getContentCode());
		writeLength(b.length);
		write(b, 0, b.length);
	}

	public void writeChunk(Chunk chunk) throws IOException
	{

		if(chunk == null)
		{
			throw new NullPointerException("Cannot write null Chunk");
		}

		if(chunk instanceof ByteChunk)
		{
			writeByteChunk((ByteChunk) chunk);
		}
		else if(chunk instanceof ShortChunk)
		{
			writeShortChunk((ShortChunk) chunk);
		}
		else if(chunk instanceof IntChunk)
		{
			writeIntChunk((IntChunk) chunk);
		}
		else if(chunk instanceof LongChunk)
		{
			writeLongChunk((LongChunk) chunk);
		}
		else if(chunk instanceof StringChunk)
		{
			writeStringChunk((StringChunk) chunk);
		}
		else if(chunk instanceof DateChunk)
		{
			writeDateChunk((DateChunk) chunk);
		}
		else if(chunk instanceof VersionChunk)
		{
			writeVersionChunk((VersionChunk) chunk);
		}
		else if(chunk instanceof ContainerChunk)
		{
			writeContainerChunk((ContainerChunk) chunk);
		}
		else
		{
			throw new RuntimeException("Unknown Chunk Type: " + chunk + ", " + chunk.getClass());
		}
	}
}
