package org.dyndns.jkiddo.raop.client;

import org.dyndns.jkiddo.raop.client.model.Device;

public interface ISpeakerListener
{
	public final static String AIRPORT = "_airport._tcp.local.";
	public final static String RAOP_TYPE = "_airport._tcp.local.";

	void removeAvailableSpeaker(String server, int port);

	void registerAvailableSpeaker(Device device);

}
