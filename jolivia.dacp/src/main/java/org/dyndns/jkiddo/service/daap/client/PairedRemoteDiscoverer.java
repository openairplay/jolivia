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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jmdns.JmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.ServiceEvent;

import org.dyndns.jkiddo.IDiscoverer;
import org.dyndns.jkiddo.service.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.server.ITouchAbleServerResource;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class PairedRemoteDiscoverer implements IDiscoverer
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PairedRemoteDiscoverer.class);

	private final IZeroconfManager mDNS;
	private final IPairingDatabase database;
	private final IClientSessionListener clientSessionListener;

	@Inject
	public PairedRemoteDiscoverer(final IZeroconfManager mDNS, final IPairingDatabase database, final IClientSessionListener clientSessionListener)
	{
		this.mDNS = mDNS;
		this.database = database;
		this.clientSessionListener = clientSessionListener;
		this.mDNS.addServiceListener(ITouchAbleServerResource.TOUCH_ABLE_SERVER, this);
		this.mDNS.addServiceListener(ITouchAbleServerResource.DACP_TYPE, this);
		this.mDNS.addServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT, this);
		this.mDNS.addNetworkTopologyListener(this);
	}

	@Override
	public void serviceAdded(final ServiceEvent event)
	{
		LOGGER.info("ADD: " + event.getDNS().getServiceInfo(event.getType(), event.getName()));
		//serviceResolved(event);
	}

	@Override
	public void serviceRemoved(final ServiceEvent event)
	{
		LOGGER.info("REMOVE: " + event.getDNS().getServiceInfo(event.getType(), event.getName()));
		final String code = database.findCode(event.getInfo().getName());
		if(code != null)
		{
			LOGGER.debug("Unpairing ... ");
			this.clientSessionListener.tearDownSession(event.getInfo().getServer(), event.getInfo().getPort());	
		}
	}

	@Override
	public void serviceResolved(final ServiceEvent event)
	{
		LOGGER.info("ADD: " + event.getDNS().getServiceInfo(event.getType(), event.getName()));

		final String code = database.findCode(event.getInfo().getName());
		if(code != null)
		{
			try
			{
				LOGGER.debug("About to pair with " + event.getInfo().getServer());
				// If code is != null, we previously have been paired with this library. It does not mean that we still are.
				final String host;
				// if(!Strings.isNullOrEmpty(event.getInfo().getServer()))
				// host = event.getInfo().getServer();
				// else
					host = event.getInfo().getHostAddresses()[0];

				this.clientSessionListener.registerNewSession(new Session(host, event.getInfo().getPort(), code));
			}
			catch(final Exception e)
			{
				LOGGER.warn("Could not establish session with client - erasing previously submitted code", e);
				database.updateCode(event.getInfo().getName(), null);
			}
		}
		else
		{
			LOGGER.debug("No matching code could be found to service: " + event.getInfo().getName());
		}
	}

	@Override
	public void inetAddressAdded(final NetworkTopologyEvent event)
	{
		final JmDNS mdns = event.getDNS();
		final InetAddress address = event.getInetAddress();
		LOGGER.info("Registered PairedRemoteDiscoverer @ " + address.getHostAddress());
		mdns.addServiceListener(ITouchAbleServerResource.TOUCH_ABLE_SERVER, this);
		mdns.addServiceListener(ITouchAbleServerResource.DACP_TYPE, this);
		mdns.addServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT, this);
	}

	@Override
	public void inetAddressRemoved(final NetworkTopologyEvent event)
	{
		final JmDNS mdns = event.getDNS();
		mdns.removeServiceListener(ITouchAbleServerResource.TOUCH_ABLE_SERVER, this);
		mdns.removeServiceListener(ITouchAbleServerResource.DACP_TYPE, this);
		mdns.removeServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT, this);
		mdns.unregisterAllServices();
	}
}
