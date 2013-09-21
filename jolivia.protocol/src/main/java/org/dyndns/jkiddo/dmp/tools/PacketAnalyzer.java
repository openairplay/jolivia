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

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;

import org.dyndns.jkiddo.dmp.DmapInputStream;
import org.dyndns.jkiddo.dmp.chunks.AbstractChunk;
import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;
import org.dyndns.jkiddo.dmp.chunks.Chunk;

public class PacketAnalyzer
{

	private static Chunk newChunk(DmapInputStream in) throws Throwable
	{
		Chunk chunk = in.getChunk();
		if(chunk instanceof BooleanChunk)
		{
			((BooleanChunk) chunk).setValue(true);
		}
		return chunk;
	}

	private static String process(File file) throws Throwable
	{
		StringBuffer buffer = new StringBuffer();
		DmapInputStream in = null;

		try
		{
			in = new DmapInputStream(new FileInputStream(file));
			while(in.available() > 0)
			{
				process(buffer, 0, in);
			}
		}
		finally
		{
			if(in != null)
			{
				in.close();
			}
		}

		return buffer.toString();
	}

	private static void process(StringBuffer buffer, int indent, DmapInputStream in) throws Throwable
	{
		AbstractChunk chunk = (AbstractChunk) newChunk(in);
		buffer.append(chunk.toString(indent)).append("\n");
	}

	public static void main(String[] args)
	{
		try
		{
			File file = null;

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

			file = new File(args[0]);
			String result = process(file);
			System.out.println(result);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			System.exit(0);
		}
	}
}
