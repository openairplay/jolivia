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
package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeMk)
public class ExtendedMediaKind extends UIntChunk
{
	//Probably resembles DatabaseShareType values
	public static final int UNKNOWN_ONE = 0x01;
	
	public ExtendedMediaKind()
	{
		this(0);
	}

	public ExtendedMediaKind(int value)
	{
		super("aeMk", "com.apple.itunes.extended-media-kind", value);
	}
}
