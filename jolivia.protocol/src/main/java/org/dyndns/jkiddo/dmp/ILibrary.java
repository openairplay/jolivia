package org.dyndns.jkiddo.dmp;

import java.util.Set;

public interface ILibrary
{

	/**
	 * Returns the current revision of this library.
	 */
	public abstract int getRevision();

	/**
	 * Sets the name of this Library. Note: Library must be open or an <tt>IllegalStateException</tt> will be thrown
	 */

	/**
	 * Returns the name of this Library
	 */
	public abstract String getName();

	/**
	 * @return
	 */
	public abstract Set<IDatabase> getDatabases();

	/**
	 * Adds database to this Library (<b>NOTE</b>: only one Database per Library is supported by iTunes!)
	 * 
	 * @param database
	 * @throws DaapTransactionException
	 */
	public abstract void addDatabase(Transaction txn, Database database);

	/**
	 * Removes database from this Library
	 * 
	 * @param database
	 * @throws DaapTransactionException
	 */

	/**
	 * Returns true if this Library contains database
	 * 
	 * @param database
	 * @return
	 */





	public abstract String toString();



	public abstract IDatabase getDatabase(long databaseId);

}