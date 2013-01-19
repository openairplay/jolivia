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

package org.dyndns.jkiddo.protocol.dmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Transaction object is the handle for a transaction. Methods off the transaction handle are used to abort and commit the transaction.
 * 
 * @author Roger Kapsi
 */
public class Transaction
{

	protected final Map<Object, List<Txn>> txnMap = new HashMap<Object, List<Txn>>();

	protected final Library library;

	protected boolean open = false;

	public Transaction(Library library)
	{
		this.library = library;
		this.open = true;
	}

	public Library getLibrary()
	{
		return library;
	}

	/**
	 * Returns <code>true</code> if this Transaction is open what means that it hasn't been commited yet.
	 * 
	 * @return <code>true</code> if this Transaction is open
	 */
	public synchronized boolean isOpen()
	{
		return open;
	}

	/**
	 * Commit this Transaction
	 */
	public synchronized void commit()
	{
		if(!isOpen())
			throw new DmapException("Transaction is not open");

		try
		{
			if(!txnMap.isEmpty())
			{
				synchronized(library)
				{
					for(List<Txn> list : txnMap.values())
					{
						for(Txn txn : list)
						{
							txn.commit(this);
						}
					}
					library.commit(this);
				}
			}
		}
		finally
		{
			close();
		}
	}

	/**
	 * Rollback this Transaction
	 */
	public synchronized void rollback()
	{
		if(!isOpen())
			throw new DmapException("Transaction is not open");

		try
		{
			if(!txnMap.isEmpty())
			{
				synchronized(library)
				{
					for(List<Txn> list : txnMap.values())
					{
						for(Txn txn : list)
						{
							txn.rollback(this);
						}
					}
					library.rollback(this);
				}
			}
		}
		finally
		{
			close();
		}
	}

	/**
	 * Attaches Object and Txn to this Transaction. Object must be an instance of Song, Playlist, Database or Library!
	 */
	public synchronized void addTxn(Object obj, Txn txn)
	{
		// if (!isOpen()) {
		// throw new DaapException("Transaction is not open");
		// }

		List<Txn> list = txnMap.get(obj);
		if(list == null || list == Collections.EMPTY_LIST)
		{
			list = new ArrayList<Txn>();
			txnMap.put(obj, list);
		}
		list.add(txn);
	}

	/**
	 * Attach Object to Transaction. This is necessary for Objects that were constructed/modified independently from this transaction.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void attach(Object obj)
	{
		if(!txnMap.containsKey(obj))
		{
			txnMap.put(obj, Collections.EMPTY_LIST);
		}
	}

	/**
	 * Returns true if Library or one of its Databases was modified
	 */
	@SuppressWarnings("unchecked")
	protected synchronized boolean modified(Library library)
	{
		if(txnMap.containsKey(library))
		{
			return true;
		}

		for(Database database : library.getDatabases())
		{
			if(modified(database))
			{
				txnMap.put(library, Collections.EMPTY_LIST);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if Database or one of its Playlists was modified
	 */
	@SuppressWarnings("unchecked")
	public synchronized boolean modified(Database database)
	{
		if(txnMap.containsKey(database))
		{
			return true;
		}

		Iterator<Playlist> it = database.getPlaylists().iterator();
		while(it.hasNext())
		{
			if(modified(it.next()))
			{
				txnMap.put(database, Collections.EMPTY_LIST);
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if Playlist or one of its Songs was modified
	 */
	public synchronized boolean modified(Playlist playlist)
	{
		if(txnMap.containsKey(playlist))
		{
			return true;
		}

		for(Song song : playlist.getSongs())
		{
			if(modified(song))
			{
				List<Txn> empty = Collections.emptyList();
				txnMap.put(playlist, empty);
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if Song was modified
	 */
	public synchronized boolean modified(Song song)
	{
		return txnMap.containsKey(song);
	}

	/**
	 * Cleanup
	 */
	protected void close()
	{
		if(open)
		{

			library.close(this);

			open = false;

			if(txnMap != null)
			{
				txnMap.clear();
			}
		}
	}

	@Override
	public String toString()
	{
		return "Transaction: " + library;
	}
}
