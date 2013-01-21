package org.dyndns.jkiddo;

import org.dyndns.jkiddo.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.daap.client.Library;
import org.dyndns.jkiddo.daap.client.RemoteControl;
import org.dyndns.jkiddo.daap.client.Session;

public class ClientSessionListener implements IClientSessionListener
{

	@Override
	public void registerNewSession(Session session) throws Exception
	{
		RemoteControl remoteControl = session.getRemoteControl();
		Library library = session.getLibrary();

		// Now do stuff :)

		remoteControl.play();
		library.getAllAlbums();
	}
}
