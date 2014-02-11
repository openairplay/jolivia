package org.dyndns.jkiddo.dmp;

import java.util.Collection;

public interface IDatabase
{

	/**
	 * Returns the unique id of this Database
	 * 
	 * @return unique id of this Database
	 */
	public abstract long getItemId();

	/**
	 * Returns the name of this Database.
	 * 
	 * @return name of this Database
	 */
	public abstract String getName();

	/**
	 * The persistent id of this Database. Unused at the moment!
	 * 
	 * @return the persistent id of this Database
	 */
	public abstract long getPersistentId();

	/**
	 * Returns the master Playlist. The master Playlist is created automatically by the Database! There's no technical difference between a master Playlist and a usual Playlist except that it cannot be removed from the Database.
	 * 
	 * @return the master Playlist
	 */
	public abstract Container getMasterContainer();

	/**
	 * Returns an unmodifiable Set with all Playlists in this Database
	 * 
	 * @return unmodifiable Set of Playlists
	 */
	public abstract Collection<Container> getContainers();

	/**
	 * Adds playlist to this Database
	 * 
	 * @param txn
	 *            a Transaction
	 * @param playlist
	 *            the Playliost to add
	 */
	public abstract void addPlaylist(Transaction txn, Container playlist);

	/**
	 * Removes playlist from this Database
	 * 
	 * @param txn
	 *            a Transaction
	 * @param playlist
	 *            the Playlist to remove
	 */

	/**
	 * Returns the number of Playlists in this Database
	 */
	public abstract int getPlaylistCount();

	/**
	 * Returns true if playlist is in this Database
	 * 
	 * @param playlist
	 * @return true if Database contains playlist
	 */



	/**
	 * Returns all Songs in this Database
	 */
	public abstract Collection<MediaItem> getItems();

	/**
	 * Returns the number of Songs in this Database
	 */
	public abstract int getSongCount();

	/**
	 * Returns true if song is in this Database
	 */

	/**
	 * Adds Song to all Playlists
	 * 
	 * @param txn
	 * @param song
	 */

	public abstract void setMediaItems(Transaction txn, Collection<MediaItem> songs);

	/**
	 * Removes Song from all Playlists
	 * 
	 * @param txn
	 * @param song
	 */


	/**
	 * Gets and returns a Song by its ID
	 * 
	 * @param songId
	 * @return
	 */

	/**
	 * Gets and returns a Playlist by its ID
	 * 
	 * @param playlistId
	 * @return
	 */
	public abstract Container getContainer(long playlistId);

}