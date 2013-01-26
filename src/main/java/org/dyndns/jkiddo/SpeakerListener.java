package org.dyndns.jkiddo;

import org.dyndns.jkiddo.raop.client.ISpeakerListener;
import org.dyndns.jkiddo.raop.client.command.StatusCommand;
import org.dyndns.jkiddo.raop.client.command.StopCommand;
import org.dyndns.jkiddo.raop.client.model.Device;
import org.dyndns.jkiddo.raop.client.model.DeviceConnection;
import org.dyndns.jkiddo.raop.client.model.DeviceResponse;

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
		DeviceConnection dc = new DeviceConnection(device);
//		DeviceResponse dr = dc.sendCommand(new StopCommand());
//		System.out.println(dr);
	}

}
