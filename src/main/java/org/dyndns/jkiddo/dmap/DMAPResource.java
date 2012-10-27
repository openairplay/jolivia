package org.dyndns.jkiddo.dmap;

import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public abstract class DMAPResource
{
	private JmDNS mDNS;
	protected Integer port;
	
	protected String hostname = InetAddress.getLocalHost().getHostName();

	public DMAPResource(JmDNS mDNS, Integer port) throws IOException
	{
		this.mDNS = mDNS;
		this.port = port;
		registerService();
	}

	void registerService() throws IOException
	{
		mDNS.registerService(registerServerRemote());
	}

	abstract protected ServiceInfo registerServerRemote();
}
