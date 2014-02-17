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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.dyndns.jkiddo.dmp.DmapException;
import org.dyndns.jkiddo.dmp.IDatabase;
import org.dyndns.jkiddo.dmp.ILibrary;

/**
 * @author Roger Kapsi
 */
public class Library implements ILibrary
{

	private static final AtomicLong PERISTENT_ID = new AtomicLong(1);

	/** The revision of this Library */
	private int revision = 3;

	/** Name of this Library */
	private String name;

	/** The total number of Databases in this Library */
	private int totalDatabaseCount = 0;

	/** Set of Databases */
	private final Set<IDatabase> databases = new HashSet<IDatabase>();

	/** Set of deleted Databases */
	private Set<IDatabase> deletedDatabases = null;

	/** List of listener */

	protected boolean clone = false;

	protected Library(Library library)
	{
		this.name = library.name;
		this.revision = library.revision;

		if(library.deletedDatabases != null)
		{
			this.deletedDatabases = library.deletedDatabases;
			library.deletedDatabases = null;
		}

		for(IDatabase database : library.databases)
		{
			/*if(txn.modified(database))
			{
				if(deletedDatabases == null || !deletedDatabases.contains(database))
				{
					IDatabase clone = new Database((Database) database, txn);
					databases.add(clone);
				}
			}*/
		}

		this.totalDatabaseCount = library.totalDatabaseCount;
		this.clone = true;

		init();
	}

	public Library(String name)
	{
		this.name = name;

		init();
	}

	private void init()
	{

	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#getRevision()
	 */
	@Override
	public synchronized int getRevision()
	{
		return revision;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#setName(org.dyndns.jkiddo.dmp.Transaction, java.lang.String)
	 */
	public void setName(final String name)
	{
		
		{
			setNameP(name);
		}
	}

	private void setNameP(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#getDatabases()
	 */
	@Override
	public Set<IDatabase> getDatabases()
	{
		return Collections.unmodifiableSet(databases);
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#addDatabase(org.dyndns.jkiddo.dmp.Transaction, org.dyndns.jkiddo.dmp.Database)
	 */
	@Override
	public void addDatabase(final Database database)
	{
		
		{
			addDatabaseP(database);
		}
	}

	private void addDatabaseP(Database database)
	{
		if(!databases.isEmpty())
		{
			throw new DmapException("One Database per Library is maximum.");
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

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#removeDatabase(org.dyndns.jkiddo.dmp.Transaction, org.dyndns.jkiddo.dmp.Database)
	 */
	public void removeDatabase(final Database database)
	{
		
		{
			removeDatabaseP( database);
		}
	}

	private void removeDatabaseP(Database database)
	{
		if(databases.remove(database))
		{
			totalDatabaseCount = databases.size();
			if(deletedDatabases == null)
			{
				deletedDatabases = new HashSet<IDatabase>();
			}
			deletedDatabases.add(database);
		}
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#containsDatabase(org.dyndns.jkiddo.dmp.IDatabase)
	 */
	public synchronized boolean containsDatabase(IDatabase database)
	{
		return databases.contains(database);
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#beginTransaction()
	 */
	

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#commit(org.dyndns.jkiddo.dmp.Transaction)
	 */
	

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#rollback(org.dyndns.jkiddo.dmp.Transaction)
	 */
	public synchronized void rollback()
	{
		// TODO: add code, actually do nothing...
	}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#close(org.dyndns.jkiddo.dmp.Transaction)
	 */
	public synchronized void close()
	{}

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#toString()
	 */
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

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#addLibraryListener(org.dyndns.jkiddo.dmp.LibraryListener)
	 */

	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#removeLibraryListener(org.dyndns.jkiddo.dmp.LibraryListener)
	 */
	/* (non-Javadoc)
	 * @see org.dyndns.jkiddo.dmp.ILibrary#getDatabase(long)
	 */
	@Override
	public IDatabase getDatabase(long databaseId)
	{
		for(IDatabase database : databases)
		{
			if(database.getItemId() == databaseId)
			{
				return database;
			}
		}
		return null;
	}
}
