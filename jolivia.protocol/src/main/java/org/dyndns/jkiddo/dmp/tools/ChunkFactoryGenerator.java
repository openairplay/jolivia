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

import java.util.Locale;

import org.dyndns.jkiddo.dmp.chunks.Chunk;

public class ChunkFactoryGenerator
{

	public static final String CLASS = "ChunkFactory";
	public static final String FILE = ChunkUtil.CHUNK_DIR + "/" + CLASS + ".java";

	public static final String CLASS_COMMENT = "/**\n" + " * This class is machine-made by {" + ChunkFactoryGenerator.class.getName() + "}!\n" + " * It is needed because Reflection cannot list the classes of a package so that we\n" + " * must pre-create a such list manually. This file must be rebuild whenever a class\n" + " * is removed or a class is added to the {@see de.kapsi.net.daap.chunks.impl} package.\n" + " */";

	public static void main(String[] args) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(CLASS_COMMENT);
		buffer.append("\n");

		buffer.append("package ").append(ChunkUtil.CHUNK_PACKAGE).append(";\n");
		buffer.append("\n");

		buffer.append("import java.util.HashMap;\n");
		buffer.append("import de.kapsi.net.daap.DaapUtil;\n");
		buffer.append("\n");

		buffer.append("public final class ").append(CLASS).append(" {\n\n");
		buffer.append("    private final Map<Integer, Class<? extends Chunk>> map = new HashMap<Integer, Class<? extends Chunk>>();\n\n");
		buffer.append("    public ").append(CLASS).append("() {\n");

		Chunk[] chunks = ChunkUtil.getChunks();

		for(int i = 0; i < chunks.length; i++)
		{
			Chunk chunk = chunks[i];
			String contentCode = "0x" + Integer.toHexString(chunk.getContentCode()).toUpperCase(Locale.US);
			String contentCodeString = chunk.getContentCodeString();
			// String name = chunk.getName();
			// int type = chunk.getType();

			buffer.append("        ");
			buffer.append("map.put(new Integer(").append(contentCode).append("), ").append(chunk.getClass().getName()).append(".class); //").append(contentCodeString);
			buffer.append("\n");
		}

		buffer.append("    }\n\n");

		buffer.append("    public Class<? extends Chunk> getChunkClass(Integer contentCode) {\n");
		buffer.append("        return map.get(contentCode);\n");
		buffer.append("    }\n\n");

		buffer.append("    public Chunk newChunk(int contentCode) {\n");
		buffer.append("        Class<? extends Chunk> clazz = getChunkClass(new Integer(contentCode));\n");
		buffer.append("        try {\n");
		buffer.append("            return clazz.newInstance();\n");
		buffer.append("        } catch (Exception err) {\n");
		buffer.append("            throw new RuntimeException(DaapUtil.toContentCodeString(contentCode), err);\n");
		buffer.append("        }\n");
		buffer.append("    }\n");

		buffer.append("}\n");

		System.out.println(buffer);

		// BufferedWriter out = new BufferedWriter(new FileWriter(new File(FILE)));
		// out.write(buffer.toString());
		// out.close();
	}
}
