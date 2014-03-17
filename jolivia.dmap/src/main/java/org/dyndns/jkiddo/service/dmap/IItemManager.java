package org.dyndns.jkiddo.service.dmap;

import java.sql.SQLException;

import org.dyndns.jkiddo.dmp.chunks.VersionChunk;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;

public interface IItemManager
{
	AuthenticationMethod.PasswordMethod getAuthenticationMethod();

	VersionChunk getMediaProtocolVersion();

	VersionChunk getAudioProtocolVersion();

	VersionChunk getPictureProtocolVersion();

	String getDMAPKey();

	long getSessionId(String remoteHost);

	void waitForUpdate();

	long getRevision(String remoteHost, long sessionId);

	Listing getDatabases() throws SQLException;

	byte[] getItemAsByteArray(long databaseId, long itemId);
	
	Listing getContainers(long databaseId, Iterable<String> parameters) throws SQLException;
	
	Listing getMediaItems(long databaseId, long containerId, Iterable<String> parameters) throws SQLException;

	Listing getMediaItems(long databaseId, Iterable<String> parameters) throws SQLException;
}
