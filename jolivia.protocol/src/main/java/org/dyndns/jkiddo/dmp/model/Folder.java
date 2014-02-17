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

package org.dyndns.jkiddo.dmp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dyndns.jkiddo.dmp.chunks.media.ParentContainerId;
import org.dyndns.jkiddo.dmp.chunks.unknown.HasChildContainers;

/**
 * A Folder is a Playlist of Playlists
 * 
 * @since iTunes 5.0
 * @author Roger Kapsi
 */
public class Folder extends Container
{

	/** */
	private final ParentContainerId parentContainerId = new ParentContainerId();

	// @since iTunes 5.0
	private final HasChildContainers hasChildContainers = new HasChildContainers(true);

	/** */
	private List<Container> playlists = null;

	protected Folder(Container playlist)
	{
		super(playlist);
		parentContainerId.setValue(getItemId());
		playlists = ((Folder) playlist).playlists;
		init();
	}

	public Folder(String name)
	{
		super(name);
		parentContainerId.setValue(getItemId());
		init();
	}

	private void init()
	{
		addChunk(hasChildContainers);
	}

	@Override
	public void addSong(MediaItem song)
	{
		throw new UnsupportedOperationException("Songs cannot be added to Folders");
	}

	@Override
	public void removeSong(MediaItem song)
	{
		throw new UnsupportedOperationException("Songs cannot be removed from Folders");
	}

	@Override
	public boolean containsSong(MediaItem song)
	{
		return false;
	}

	@Override
	public List<MediaItem> getItems()
	{
		return Collections.emptyList();
	}

	public void addPlaylist(final Container playlist)
	{
		if(playlist instanceof Folder)
		{
			throw new IllegalArgumentException("Recursion is not supported");
		}

		
		{
			addPlaylistP(playlist);
		}
	}

	private void addPlaylistP(Container playlist)
	{
		if(playlists == null)
		{
			playlists = new ArrayList<Container>();
		}

		if(!containsPlaylist(playlist) && playlists.add(playlist))
		{
			playlist.addChunk(parentContainerId);
		}
	}

	public void removePlaylist(final Container playlist)
	{
		if(playlist instanceof Folder)
		{
			return;
		}

		
		{
			removePlaylistP(playlist);
		}
	}

	private void removePlaylistP(Container playlist)
	{
		if(playlists == null)
		{
			return;
		}

		if(playlists.remove(playlist))
		{
			playlist.removeChunk(parentContainerId);

			if(playlists.isEmpty())
			{
				playlists = null;
			}
		}
	}

	public int getPlaylistCount()
	{
		return getPlaylists().size();
	}

	public List<Container> getPlaylists()
	{
		if(playlists != null)
		{
			return Collections.unmodifiableList(playlists);
		}
		return Collections.emptyList();
	}

	public boolean containsPlaylist(Container playlist)
	{
		return getPlaylists().contains(playlist);
	}
}
