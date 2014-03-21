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

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.model.MediaItem;

public interface IMusicStoreReader
{
	public Collection<MediaItem> readTunes() throws Exception;
	
	public void readTunesMemoryOptimized(Listing listing, Map<Long, String> map) throws Exception;

	public URI getTune(String tuneIdentifier) throws Exception;

	/*interface IMusicItem
	{
		String getArtist();

		String getAlbum();
		
		String getTitle();
		
		long getSize();

		void setArtist(String artist);

		void setAlbum(String album);

		void setTitle(String title);

		void setComposer(String composer);

		void setGenre(String genre);

		void setTrackNumber(int trackNumber);

		void setYear(int Year);

		void setSize(long size);

		long getDuration();
		
		void setDuration(long value);
	}*/
}