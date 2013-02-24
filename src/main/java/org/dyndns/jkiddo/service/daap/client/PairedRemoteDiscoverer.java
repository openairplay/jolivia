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
package org.dyndns.jkiddo.service.daap.client;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.JmmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.ServiceEvent;

import org.dyndns.jkiddo.IDiscoverer;
import org.dyndns.jkiddo.service.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.service.dacp.server.IRemoteControlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PairedRemoteDiscoverer implements IDiscoverer
{
	public static final Logger logger = LoggerFactory.getLogger(PairedRemoteDiscoverer.class);

	private final JmmDNS mDNS;
	private final Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();
	private final IPairingDatabase database;
	private final IClientSessionListener clientSessionListener;

	@Inject
	public PairedRemoteDiscoverer(JmmDNS mDNS, IPairingDatabase database, IClientSessionListener clientSessionListener)
	{
		this.mDNS = mDNS;
		this.database = database;
		this.clientSessionListener = clientSessionListener;
		this.mDNS.addServiceListener(IRemoteControlResource.TOUCH_ABLE_TYPE, this);
		this.mDNS.addServiceListener(IRemoteControlResource.DACP_TYPE, this);
		this.mDNS.addNetworkTopologyListener(this);
	}

	@Override
	public void serviceAdded(ServiceEvent event)
	{
		logger.info("ADD: " + event.getDNS().getServiceInfo(event.getType(), event.getName()));
	}

	@Override
	public void serviceRemoved(ServiceEvent event)
	{
		logger.debug("REMOVE: " + event.getName());
		this.clientSessionListener.tearDownSession(event.getInfo().getServer(), event.getInfo().getPort());
	}

	@Override
	public void serviceResolved(ServiceEvent event)
	{
		logger.info("ADD: " + event.getDNS().getServiceInfo(event.getType(), event.getName()));

		final String code = database.findCode(event.getInfo().getName());
		if(code != null)
		{
			try
			{
				logger.debug("About to pair with " + event.getInfo().getServer());
				// If code is != null, we previously have been paired with this library. It does not mean that we still are.
				final String host;
				if(!Strings.isNullOrEmpty(event.getInfo().getServer()))
					host = event.getInfo().getServer();
				else
					host = event.getInfo().getHostAddresses()[0];

				this.clientSessionListener.registerNewSession(new Session(host, event.getInfo().getPort(), code));
			}
			catch(Exception e)
			{
				logger.warn(e.getMessage(), e);
				database.updateCode(event.getInfo().getName(), null);
			}
		}
		else
		{
			logger.debug("No matching code could be found to service: " + event.getInfo().getName());
		}
	}

	@Override
	public void inetAddressAdded(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		InetAddress address = event.getInetAddress();
		logger.info("Registered PairedRemoteDiscoverer @ " + address.getHostAddress());
		mdns.addServiceListener(IRemoteControlResource.TOUCH_ABLE_TYPE, this);
		mdns.addServiceListener(IRemoteControlResource.DACP_TYPE, this);
		interfaces.put(mdns, address);
	}

	@Override
	public void inetAddressRemoved(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		mdns.removeServiceListener(IRemoteControlResource.TOUCH_ABLE_TYPE, this);
		mdns.removeServiceListener(IRemoteControlResource.DACP_TYPE, this);
		mdns.unregisterAllServices();
		interfaces.remove(mdns);
	}
}
