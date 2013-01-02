/*******************************************************************************
 * Copyright (c) 2012 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - initial API and implementation
 ******************************************************************************/
package org.dyndns.jkiddo.dacp.client;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.JmmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.NetworkTopologyListener;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import org.dyndns.jkiddo.daap.client.Session;
import org.dyndns.jkiddo.dacp.server.IRemoteControlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PairingDaemon implements ServiceListener, NetworkTopologyListener
{
	public static final Logger logger = LoggerFactory.getLogger(PairingDaemon.class);

	private final JmmDNS mDNS;
	private final Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();
	private final IDatabase database;

	@Inject
	public PairingDaemon(JmmDNS mDNS, IDatabase database)
	{
		this.mDNS = mDNS;
		this.database = database;
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
				spawnClientTraverser(event.getInfo().getServer(), code);
			}
			catch(Exception e)
			{
				logger.warn(e.getMessage(), e);
				database.updateCode(event.getInfo().getName(), null);
			}
		}
	}

	private void spawnClientTraverser(String server, String code) throws Exception
	{
		Session session = new Session(server, code);

	}

	@Override
	public void inetAddressAdded(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		InetAddress address = event.getInetAddress();
		logger.info("Registered Pairing Service @ " + address.getHostAddress());
		mdns.addServiceListener(IRemoteControlResource.TOUCH_ABLE_TYPE, this);
		// mdns.addServiceListener(IPairingResource.REMOTE_TYPE, this);
		interfaces.put(mdns, address);
	}

	@Override
	public void inetAddressRemoved(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		mdns.removeServiceListener(IRemoteControlResource.TOUCH_ABLE_TYPE, this);
		// mdns.removeServiceListener(IPairingResource.REMOTE_TYPE, this);
		mdns.unregisterAllServices();
		interfaces.remove(mdns);
	}
}
