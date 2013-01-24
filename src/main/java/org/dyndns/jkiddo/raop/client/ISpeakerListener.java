package org.dyndns.jkiddo.raop.client;

import org.dyndns.jkiddo.raop.client.model.Device;

public interface ISpeakerListener
{
//	public final static String AIR_PLAY = "_airplay._tcp.local.";
	public final static String AIR_PORT = "_airport._tcp.local.";

	void removeAvailableSpeaker(String server, int port);

	void registerAvailableSpeaker(Device device);

}
