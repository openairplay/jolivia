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
package org.dyndns.jkiddo.dpap.chunks.picture;

import org.dyndns.jkiddo.dmp.chunks.VersionChunk;

/**
 * DPAP.ProtocolVersion Represents the protocol version of the current implemented DPAP 'standard'.
 * 
 * @author Charles Ikeson
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ppro)
public class PictureProtocolVersion extends VersionChunk
{
	public PictureProtocolVersion()
	{
		this(0);
	}

	public PictureProtocolVersion(int version)
	{
		super("ppro", "dpap.protocolversion", version);
	}

	public PictureProtocolVersion(int major, int minor, int patch)
	{
		super("ppro", "dpap.protocolversion", major, minor, patch);
	}
}
