package org.dyndns.jkiddo;

import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.Library;
import org.dyndns.jkiddo.service.daap.client.RemoteControl;
import org.dyndns.jkiddo.service.daap.client.Session;

public class ClientSessionListener implements IClientSessionListener
{

	@Override
	public void registerNewSession(Session session) throws Exception
	{
		RemoteControl remoteControl = session.getRemoteControl();
		Library library = session.getLibrary();

		// Now do stuff :)
		remoteControl.play();
		remoteControl.getNowPlaying();
		library.getAllAlbums();
	}

	@Override
	public void tearDownSession(String hostname, int port)
	{
		
	}
}
