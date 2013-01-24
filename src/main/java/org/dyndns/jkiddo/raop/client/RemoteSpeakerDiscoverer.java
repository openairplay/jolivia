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
package org.dyndns.jkiddo.raop.client;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.JmmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.ServiceEvent;

import org.dyndns.jkiddo.IDiscoverer;
import org.dyndns.jkiddo.raop.client.service.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RemoteSpeakerDiscoverer implements IDiscoverer
{
	public static final Logger logger = LoggerFactory.getLogger(RemoteSpeakerDiscoverer.class);

	private final JmmDNS mDNS;
	private final Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();
	private final ISpeakerListener speakerListener;

	@Inject
	public RemoteSpeakerDiscoverer(JmmDNS mDNS, ISpeakerListener speakerListener)
	{
		this.mDNS = mDNS;
		this.speakerListener = speakerListener;
//		this.mDNS.addServiceListener(ISpeakerListener.AIR_PLAY, this);
		this.mDNS.addServiceListener(ISpeakerListener.AIR_PORT, this);
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
		this.speakerListener.removeAvailableSpeaker(event.getInfo().getServer(), event.getInfo().getPort());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void serviceResolved(ServiceEvent event)
	{
		logger.info("ADD: " + event.getDNS().getServiceInfo(event.getType(), event.getName()));
		speakerListener.registerAvailableSpeaker(new Device(event.getName(), event.getInfo().getInetAddress(), event.getInfo().getPort()));
	}

	@Override
	public void inetAddressAdded(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		InetAddress address = event.getInetAddress();
		logger.info("Registered RemoteSpeakerDiscoverer @ " + address.getHostAddress());
//		mdns.addServiceListener(ISpeakerListener.AIR_PLAY, this);
		mdns.addServiceListener(ISpeakerListener.AIR_PORT, this);
		interfaces.put(mdns, address);
	}

	@Override
	public void inetAddressRemoved(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
//		mdns.removeServiceListener(ISpeakerListener.AIR_PLAY, this);
		mdns.removeServiceListener(ISpeakerListener.AIR_PORT, this);
		mdns.unregisterAllServices();
		interfaces.remove(mdns);
	}
}
