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
package org.dyndns.jkiddo.daap.server;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.protocol.dmap.Database;
import org.dyndns.jkiddo.protocol.dmap.Library;
import org.dyndns.jkiddo.protocol.dmap.Song;
import org.dyndns.jkiddo.protocol.dmap.Transaction;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MusicLibraryManager
{
	@Inject
	private Set<IMusicStoreReader> setOfReaders;
	private Library library;
	private Database database;
	private Map<Integer, IMusicStoreReader> songToReader;

	@Inject
	public MusicLibraryManager(Set<IMusicStoreReader> databases) throws Exception
	{
		songToReader = new HashMap<Integer, IMusicStoreReader>();
		library = new Library(LIBARY_NAME);
		database = new Database(LIBARY_NAME);
		Transaction txn = library.beginTransaction();

		Set<Song> collectionOfSongs = new HashSet<Song>();
		for(IMusicStoreReader reader : databases)
		{
			Collection<Song> songs = reader.readTunes();
			collectionOfSongs.addAll(songs);
			for(Song s : songs)
			{
				songToReader.put(s.hashCode(), reader);
			}
		}

		database.setSongs(txn, collectionOfSongs);
		library.addDatabase(txn, database);
		txn.commit();
	}

	private static final String LIBARY_NAME = Jolivia.name;

	public enum PasswordMethod
	{
		NO_PASSWORD, PASSWORD, USERNAME_AND_PASSWORD
	}

	@SuppressWarnings("unused")
	public int getRevision(String remoteHost, long session_id)
	{
		return library.getRevision();
	}

	@SuppressWarnings("unused")
	public int getSessionId(String remoteHost)
	{
		return new Random().nextInt(Integer.MAX_VALUE);
	}

	public PasswordMethod getAuthenticationMethod()
	{
		return PasswordMethod.NO_PASSWORD;
	}

	public int getNrOfDatabases()
	{
		// only one Database per Library is supported by iTunes
		return library.getDatabases().size();
	}

	public String getName()
	{
		return library.getName();
	}

	public String getLibraryName()
	{
		return LIBARY_NAME;
	}

	public File getTune(long databaseId, long itemId) throws Exception
	{
		IMusicStoreReader reader = songToReader.get(new Integer((int) itemId));
		Song song = library.getDatabase(databaseId + "").getSong(itemId + "");
		return reader.getTune(song);
	}

	public Collection<Database> getDatabases()
	{
		return library.getDatabases();
	}

	public Database getDatabase(long databaseId)
	{
		return library.getDatabase(databaseId + "");
	}

	public void waitForUpdate()
	{
		try
		{
			Thread.sleep(100000000);
		}
		catch(InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}
}
