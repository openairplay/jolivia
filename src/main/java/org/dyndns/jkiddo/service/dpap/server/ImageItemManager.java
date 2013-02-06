package org.dyndns.jkiddo.service.dpap.server;

import java.io.File;
import java.util.Collection;

import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.chunks.VersionChunk;
import org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.service.dmap.IItemManager;

public class ImageItemManager implements IItemManager
{

	@Override
	public PasswordMethod getAuthenticationMethod()
	{
		return PasswordMethod.NO_PASSWORD;
	}

	@Override
	public VersionChunk getDmapProtocolVersion()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionChunk getDaapProtocolVersion()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionChunk getDpapProtocolVersion()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDMAPKey()
	{
		return "DPAP-Server";
	}

	@Override
	public long getSessionId(String remoteHost)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void waitForUpdate()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public long getRevision(String remoteHost, long sessionId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Database> getDatabases()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Database getDatabase(long databaseId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getItemAsFile(long databaseId, long itemId)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
