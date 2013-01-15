/*******************************************************************************
 * Copyright (c) 2012 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - initial API and implementation
 ******************************************************************************/
package org.dyndns.jkiddo.logic.interfaces;

import java.io.File;
import java.util.Collection;

import org.dyndns.jkiddo.protocol.dmap.Song;

public interface IMusicStoreReader
{
	public Collection<Song> readTunes() throws Exception;

	public File getTune(Song tune) throws Exception;

	public String getLibraryName();
}
