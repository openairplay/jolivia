package org.dyndns.jkiddo;

import javax.inject.Singleton;

import org.dyndns.jkiddo.raop.ISpeakerListener;
import org.dyndns.jkiddo.raop.client.model.DeviceConnection;

@Singleton
public class SpeakerListener implements ISpeakerListener
{
	@Override
	public void removeAvailableSpeaker(String server, int port)
	{
		// TODO Auto-generated method stub
		System.out.println(server);
	}

	@Override
	public void registerAvailableSpeaker(DeviceConnection device)
	{
		DeviceConnection dc = device;
//		DeviceResponse dr = dc.sendCommand(new StopCommand());
//		System.out.println(dr);
	}

}
