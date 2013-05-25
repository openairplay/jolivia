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

package org.dyndns.jkiddo.dmap.tools;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dyndns.jkiddo.dmap.ByteUtil;
import org.dyndns.jkiddo.dmap.chunks.Chunk;

/**
 * This tool will help you to detect changes in the DAAP protocol.
 * <p>
 * <ol>
 * <li>install ethereal
 * <li>capture the /content-codes request between two iTunes hosts
 * <li>save the data as foobar.gz
 * <li>run gzip -d foobar.gz
 * <li>open foobar with this tool and it will tell you what's new!
 * </ol>
 * </p>
 * 
 * @author Roger Kapsi
 */
public class ContentCodesAnalyzer
{

	/** Creates a new instance of ContentCodesAnalyzer */
	public ContentCodesAnalyzer()
	{}

	private static Map<String, ContentCode> getContentCodes() throws Exception
	{
		Map<String, ContentCode> map = new HashMap<String, ContentCode>();

		Chunk[] chunks = ChunkUtil.getChunks();
		for(int i = 0; i < chunks.length; i++)
		{
			Chunk chunk = chunks[i];
			String contentCode = chunk.getContentCodeString();
			String name = chunk.getName();
			int type = chunk.getType();

			map.put(contentCode, new ContentCode(contentCode, name, type));
		}

		return map;
	}

	private static Map<String, ContentCode> readNewChunks(File file) throws IOException
	{
		Map<String, ContentCode> map = new HashMap<String, ContentCode>();

		FileInputStream in = new FileInputStream(file);

		try
		{
			in.skip(0x14); // skip header
			in.skip(0x04); // skip 'mdcl'

			byte[] lenBuf = new byte[4];

			while(in.read(lenBuf, 0, lenBuf.length) != -1)
			{
				int len = ByteUtil.toIntBE(lenBuf, 0);
				byte[] buf = new byte[len];
				if(in.read(buf, 0, buf.length) == -1)
					break;

				int pos = 0;

				pos += 4; // skip 'mcnm'
				len = ByteUtil.toIntBE(buf, pos);
				pos += 4;
				String contentCode = new String(buf, pos, len);
				pos += len;

				pos += 4; // skip 'mcna'
				len = ByteUtil.toIntBE(buf, pos);
				pos += 4;
				String name = new String(buf, pos, len);
				pos += len;

				pos += 4; // skip 'mcty'
				len = ByteUtil.toIntBE(buf, pos);
				pos += 4;
				int type = ByteUtil.toInt16BE(buf, pos);
				pos += len;

				map.put(contentCode, new ContentCode(contentCode, name, type));

				in.skip(0x04); // skip 'mdcl' of the next chunk
			}

		}
		finally
		{
			in.close();
		}

		return map;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws Exception
	{

		if(args.length == 0)
		{
			FileDialog dialog = new FileDialog(new Frame(), "Select file...", FileDialog.LOAD);
			dialog.setVisible(true);

			String d = dialog.getDirectory();
			String f = dialog.getFile();

			if(d != null && f != null)
			{
				args = new String[] { d + f };
			}
			else
			{
				System.out.println("No file selected... Bye!");
				System.exit(0);
			}
		}

		Map<String, ContentCode> knownChnunks = getContentCodes();

		Map<String, ContentCode> newChunks = readNewChunks(new File(args[0]));

		Iterator<String> it = null;

		/*
		 * System.out.println("\n+++ KNOWN CHUNKS +++\n"); it = knownChnunks.keySet().iterator(); while(it.hasNext()) { System.out.println(knownChnunks.get(it.next())); } System.out.println("\n+++ NEW CHUNKS +++\n"); it = newChunks.keySet().iterator(); while(it.hasNext()) { System.out.println(newChunks.get(it.next())); }
		 */

		List<ContentCode> added = new ArrayList<ContentCode>();
		List<ContentCode> removed = new ArrayList<ContentCode>();
		List<ContentCode[]> changed = new ArrayList<ContentCode[]>();

		it = newChunks.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			ContentCode obj = newChunks.get(key);

			if(knownChnunks.containsKey(key) == false)
			{
				added.add(obj);
			}
			else
			{
				ContentCode obj2 = knownChnunks.get(key);
				if(obj2.equals(obj) == false)
				{
					changed.add(new ContentCode[] { obj, obj2 });
				}
			}
		}

		it = knownChnunks.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			ContentCode obj = knownChnunks.get(key);
			if(newChunks.containsKey(key) == false)
			{
				removed.add(obj);
			}
		}

		System.out.println("\n+++ NEW CHUNKS +++\n");

		Iterator<ContentCode> iter = added.iterator();
		while(iter.hasNext())
		{
			System.out.println(iter.next());
		}

		System.out.println("\n+++ REMOVED CHUNKS +++\n");

		iter = removed.iterator();
		while(iter.hasNext())
		{
			System.out.println(iter.next());
		}

		System.out.println("\n+++ CHANGED CHUNKS +++\n");

		Iterator<ContentCode[]> iter2 = changed.iterator();
		while(iter2.hasNext())
		{
			ContentCode[] obj = iter2.next();

			System.out.println("NEW: " + obj[0]);
			System.out.println("OLD: " + obj[1]);
		}

		/*
		 * FileOutputStream os = new FileOutputStream(new File( "/Users/roger/foobar.txt")); byte[] dst = new byte[4]; ByteUtil.toByteBE(SongCodecType.MPEG, dst, 0); os.write(dst, 0, dst.length);
		 */
	}

	private static final class ContentCode
	{

		private String contentCode;
		private String name;
		private int type;

		private ContentCode(String contentCode, String name, int type)
		{
			this.contentCode = contentCode;
			this.name = name;
			this.type = type;
		}

		@Override
		public boolean equals(Object o)
		{
			ContentCode other = (ContentCode) o;

			return(contentCode.equals(other.contentCode) && name.equals(other.name) && type == other.type);
		}

		@Override
		public String toString()
		{
			StringBuffer buf = new StringBuffer();

			buf.append("dmap.dictionary = {\n");
			buf.append("    dmap.contentcodesnumber = ").append(contentCode).append("\n");
			buf.append("    dmap.contentcodesname = ").append(name).append("\n");
			buf.append("    dmap.contentcodestype = ").append(type).append("\n");
			buf.append("}\n");

			return buf.toString();
		}
	}
}
