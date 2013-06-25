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
package org.dyndns.jkiddo.logic.interfaces;

import java.io.File;
import java.util.Set;

public interface IMusicStoreReader
{
	public Set<IMusicItem> readTunes() throws Exception;

	public File getTune(IMusicItem tune) throws Exception;

	interface IMusicItem
	{

		String getArtist();

		String getAlbum();
		
		String getName();
		
		long getSize();

		void setArtist(String artist);

		void setAlbum(String album);

		void setName(String name);

		void setComposer(String composer);

		void setGenre(String genre);

		void setTrackNumber(int trackNumber);

		void setYear(int Year);

		void setSize(long size);

	}
}
