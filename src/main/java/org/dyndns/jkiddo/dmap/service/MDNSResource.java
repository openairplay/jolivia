/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
package org.dyndns.jkiddo.dmap.service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.JmmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.NetworkTopologyListener;
import javax.jmdns.ServiceInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MDNSResource implements NetworkTopologyListener
{
	private JmmDNS mDNS;
	protected Integer port;

	public static final Logger logger = LoggerFactory.getLogger(MDNSResource.class);
	private final Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();
	protected final String hostname = InetAddress.getLocalHost().getHostName();
	private ServiceInfo serviceInfo;

	public MDNSResource(JmmDNS mDNS, Integer port) throws IOException
	{
		this.mDNS = mDNS;
		this.port = port;
		this.mDNS.addNetworkTopologyListener(this);
	}

	//http://www.dns-sd.org/ServiceTypes.html
	abstract protected ServiceInfo getServiceInfoToRegister();

	protected synchronized void registerServiceInfo() throws IOException
	{
		serviceInfo = getServiceInfoToRegister();

		for(JmDNS mdns : interfaces.keySet())
		{
			mdns.registerService(serviceInfo);
		}
	}

	@Override
	public synchronized void inetAddressAdded(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		InetAddress address = event.getInetAddress();
		try
		{
			logger.info("Registering service: " + serviceInfo.getQualifiedName());
			mdns.registerService(serviceInfo);
		}
		catch(IOException e)
		{
			logger.error(e.getMessage(), e);
		}
		interfaces.put(mdns, address);
	}

	@Override
	public synchronized void inetAddressRemoved(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		mdns.unregisterService(serviceInfo);
		mdns.unregisterAllServices();
		interfaces.remove(mdns);
	}
}
