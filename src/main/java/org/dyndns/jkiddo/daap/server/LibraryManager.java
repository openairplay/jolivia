package org.dyndns.jkiddo.daap.server;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.ardverk.daap.Database;
import org.ardverk.daap.Library;
import org.ardverk.daap.Song;
import org.ardverk.daap.Transaction;
import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LibraryManager
{
	@Inject
	private Set<IMusicStoreReader> setOfReaders;
	private Library library;
	private Database database;
	private Map<Integer,IMusicStoreReader> songToReader;

	@Inject
	public LibraryManager(Set<IMusicStoreReader> databases) throws Exception
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

	private static final String LIBARY_NAME = Jolivia.class.getSimpleName();

	public enum PasswordMethod
	{
		NO_PASSWORD, PASSWORD, USERNAME_AND_PASSWORD
	}

	public int getRevision(String remoteHost, String session_id)
	{
		return library.getRevision();
	}

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

	public File getTune(String databaseId, String itemId) throws Exception
	{
		IMusicStoreReader reader = songToReader.get(new Integer(itemId));
		Song song = library.getDatabase(databaseId).getSong(itemId);
		return reader.getTune(song);
	}

	public Collection<Database> getDatabases()
	{
		return library.getDatabases();
	}

	public Database getDatabase(String databaseId)
	{
		return library.getDatabase(databaseId);
	}
}
