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

package org.ardverk.daap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roger Kapsi
 */
public class Library
{

	private static final Logger LOG = LoggerFactory.getLogger(Library.class);

	private static final AtomicLong PERISTENT_ID = new AtomicLong(1);

	/** The revision of this Library */
	private int revision = 0;

	/** Name of this Library */
	private String name;

	/** The total number of Databases in this Library */
	private int totalDatabaseCount = 0;

	/** Set of Databases */
	private final Set<Database> databases = new HashSet<Database>();

	/** Set of deleted Databases */
	private Set<Database> deletedDatabases = null;

	/** List of listener */
	private final List<WeakReference<LibraryListener>> listener = new ArrayList<WeakReference<LibraryListener>>();

	protected boolean clone = false;

	protected Library(Library library, Transaction txn)
	{
		this.name = library.name;
		this.revision = library.revision;

		if(library.deletedDatabases != null)
		{
			this.deletedDatabases = library.deletedDatabases;
			library.deletedDatabases = null;
		}

		for(Database database : library.databases)
		{
			if(txn.modified(database))
			{
				if(deletedDatabases == null || !deletedDatabases.contains(database))
				{
					Database clone = new Database(database, txn);
					databases.add(clone);
				}
			}
		}

		this.totalDatabaseCount = library.totalDatabaseCount;
		this.clone = true;

		init();
	}

	public Library(String name)
	{
		this.name = name;
		commit(null);

		init();
	}

	private void init()
	{

	}

	/**
	 * Returns the current revision of this library.
	 */
	public synchronized int getRevision()
	{
		return revision;
	}

	/**
	 * Sets the name of this Library. Note: Library must be open or an <tt>IllegalStateException</tt> will be thrown
	 */
	public void setName(Transaction txn, final String name)
	{
		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					setNameP(txn, name);
				}
			});
		}
		else
		{
			setNameP(txn, name);
		}
	}

	private void setNameP(Transaction txn, String name)
	{
		this.name = name;
	}

	/**
	 * Returns the name of this Library
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return
	 */
	public Set<Database> getDatabases()
	{
		return Collections.unmodifiableSet(databases);
	}

	/**
	 * Adds database to this Library (<b>NOTE</b>: only one Database per Library is supported by iTunes!)
	 * 
	 * @param database
	 * @throws DaapTransactionException
	 */
	public void addDatabase(Transaction txn, final Database database)
	{
		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					addDatabaseP(txn, database);
				}
			});
			txn.attach(database);
		}
		else
		{
			addDatabaseP(txn, database);
		}
	}

	private void addDatabaseP(Transaction txn, Database database)
	{
		if(!databases.isEmpty())
		{
			throw new DaapException("One Database per Library is maximum.");
		}

		if(databases.add(database))
		{
			totalDatabaseCount = databases.size();
			if(deletedDatabases != null && deletedDatabases.remove(database) && deletedDatabases.isEmpty())
			{
				deletedDatabases = null;
			}
		}
	}

	/**
	 * Removes database from this Library
	 * 
	 * @param database
	 * @throws DaapTransactionException
	 */
	public void removeDatabase(Transaction txn, final Database database)
	{
		if(txn != null)
		{
			txn.addTxn(this, new Txn() {
				@Override
				public void commit(Transaction txn)
				{
					removeDatabaseP(txn, database);
				}
			});
		}
		else
		{
			removeDatabaseP(txn, database);
		}
	}

	private void removeDatabaseP(Transaction txn, Database database)
	{
		if(databases.remove(database))
		{
			totalDatabaseCount = databases.size();
			if(deletedDatabases == null)
			{
				deletedDatabases = new HashSet<Database>();
			}
			deletedDatabases.add(database);
		}
	}

	/**
	 * Returns true if this Library contains database
	 * 
	 * @param database
	 * @return
	 */
	public synchronized boolean containsDatabase(Database database)
	{
		return databases.contains(database);
	}

	public synchronized Transaction beginTransaction()
	{
		Transaction txn = new Transaction(this);
		return txn;
	}

	public synchronized void commit(Transaction txn)
	{
		if(txn == null)
		{
			txn = new Transaction(this);
			txn.addTxn(this, new Txn());
			txn.commit();
			return;
		}

		this.revision++;
		Library diff = new Library(this, txn);

		synchronized(listener)
		{
			Iterator<WeakReference<LibraryListener>> it = listener.iterator();
			while(it.hasNext())
			{
				LibraryListener l = it.next().get();
				if(l == null)
				{
					it.remove();
				}
				else
				{
					l.libraryChanged(this, diff);
				}
			}
		}
	}

	public synchronized void rollback(Transaction txn)
	{
		// TODO: add code, actually do nothing...
	}

	public synchronized void close(Transaction txn)
	{}

	@Override
	public String toString()
	{
		if(!clone)
		{
			return "Library(" + revision + ")";
		}
		return "LibraryPatch(" + revision + ")";
	}

	protected static long nextPersistentId()
	{
		return PERISTENT_ID.getAndIncrement();
	}

	public void addLibraryListener(LibraryListener l)
	{
		synchronized(listener)
		{
			listener.add(new WeakReference<LibraryListener>(l));
		}
	}

	public void removeLibraryListener(LibraryListener l)
	{
		synchronized(listener)
		{
			Iterator<WeakReference<LibraryListener>> it = listener.iterator();
			while(it.hasNext())
			{
				LibraryListener gotten = it.next().get();
				if(gotten == null || gotten == l)
				{
					it.remove();
				}
			}
		}
	}
	
	public Database getDatabase(String itemId)
	{
		long databaseId = Long.parseLong(itemId);
		for(Database database : databases)
		{
			if(database.getItemId() == databaseId)
			{
				return database;
			}
		}
		return null;
	}
}
