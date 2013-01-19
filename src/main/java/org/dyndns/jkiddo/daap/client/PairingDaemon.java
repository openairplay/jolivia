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
package org.dyndns.jkiddo.daap.client;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.JmmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.NetworkTopologyListener;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import org.dyndns.jkiddo.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.dacp.server.IRemoteControlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PairingDaemon implements ServiceListener, NetworkTopologyListener, IAutomatedClientSessionCreator
{
	public static final Logger logger = LoggerFactory.getLogger(PairingDaemon.class);

	private final JmmDNS mDNS;
	private final Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();
	private final IPairingDatabase database;

	@Inject
	public PairingDaemon(JmmDNS mDNS, IPairingDatabase database)
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
				createNewSession(event.getInfo().getServer(), event.getInfo().getPort(), code);
			}
			catch(Exception e)
			{
				logger.warn(e.getMessage(), e);
				database.updateCode(event.getInfo().getName(), null);
			}
		}
	}

	@Override
	public void createNewSession(String server, int port, String code) throws Exception
	{
		Session session = new Session(server, port, code);
		RemoteControl remoteControl = session.getRemoteControl();
		Library library = session.getLibrary();

		// Now do stuff with the remote control :-)
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
