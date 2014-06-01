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
package org.dyndns.jkiddo.service.dmap;

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
	private final JmmDNS mDNS;
	protected Integer port;

	static final Logger logger = LoggerFactory.getLogger(MDNSResource.class);
	private final Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();
	protected final String hostname = InetAddress.getLocalHost().getHostName();
	private ServiceInfo serviceInfo;

	public MDNSResource(final JmmDNS mDNS, final Integer port) throws IOException
	{
		this.mDNS = mDNS;
		this.port = port;
	}

	// http://www.dns-sd.org/ServiceTypes.html
	abstract protected ServiceInfo getServiceInfoToRegister();

	//protected void cleanup()
	public void deRegister()
	{
		this.mDNS.unregisterAllServices();
	}

	//protected synchronized void signUp() throws IOException
	public synchronized void register() throws IOException
	{
		serviceInfo = getServiceInfoToRegister();
		//serviceInfo.setText(props);x
		mDNS.registerService(serviceInfo);
//		for(JmDNS mdns : interfaces.keySet())
//		{
//			mdns.registerService(serviceInfo);
//		}
		this.mDNS.addNetworkTopologyListener(this);
	}

	@Override
	public synchronized void inetAddressAdded(final NetworkTopologyEvent event)
	{
		final JmDNS mdns = event.getDNS();
		final InetAddress address = event.getInetAddress();
		try
		{
			logger.info("Registering service: " + serviceInfo.getQualifiedName());
			mdns.registerService(serviceInfo);
		}
		catch(final IOException e)
		{
			logger.error(e.getMessage(), e);
		}
		interfaces.put(mdns, address);
	}

	@Override
	public synchronized void inetAddressRemoved(final NetworkTopologyEvent event)
	{
		final JmDNS mdns = event.getDNS();
		mdns.unregisterService(serviceInfo);
		mdns.unregisterAllServices();
		interfaces.remove(mdns);
	}
}
