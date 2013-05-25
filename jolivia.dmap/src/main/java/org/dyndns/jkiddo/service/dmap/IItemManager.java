package org.dyndns.jkiddo.service.dmap;

import java.io.File;
import java.util.Collection;

import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.chunks.VersionChunk;
import org.dyndns.jkiddo.dmap.chunks.media.AuthenticationMethod;

public interface IItemManager
{
	AuthenticationMethod.PasswordMethod getAuthenticationMethod();

	VersionChunk getDmapProtocolVersion();

	VersionChunk getDaapProtocolVersion();

	VersionChunk getProtocolVersion();

	String getDMAPKey();

	long getSessionId(String remoteHost);

	void waitForUpdate();

	long getRevision(String remoteHost, long sessionId);

	Collection<Database> getDatabases();

	Database getDatabase(long databaseId);

	File getItemAsFile(long databaseId, long itemId);

}
