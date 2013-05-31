package org.dyndns.jkiddo.raop;

import org.dyndns.jkiddo.raop.client.model.Device;

public interface ISpeakerListener
{
	public final static String RAOP_TYPE = "_raop._tcp.local.";

	void removeAvailableSpeaker(String server, int port);

	void registerAvailableSpeaker(Device device);

}
