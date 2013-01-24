package org.dyndns.jkiddo.raop.client;

import org.dyndns.jkiddo.raop.client.model.Device;

import com.google.inject.Singleton;

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
	public void registerAvailableSpeaker(Device device)
	{
		// TODO Auto-generated method stub
		System.out.println(device);
	}

}
