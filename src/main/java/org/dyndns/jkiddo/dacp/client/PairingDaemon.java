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
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.dyndns.jkiddo.dacp.server.IRemoteControlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tunesremote.IDatabase;
import org.tunesremote.daap.Library;
import org.tunesremote.daap.Session;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PairingDaemon implements ServiceListener, NetworkTopologyListener
{
	public static final Logger logger = LoggerFactory.getLogger(PairingDaemon.class);

	private JmmDNS mDNS;

	private Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();

	private IDatabase database;

	@Inject
	public PairingDaemon(JmmDNS mDNS, IDatabase database)
	{
		this.mDNS = mDNS;
		this.mDNS.addNetworkTopologyListener(this);
		this.database = database;
	}

	@Override
	public void serviceAdded(ServiceEvent event)
	{
		logger.info("ADD: " + event.getDNS().getServiceInfo(event.getType(), event.getName()));
		final String serviceName = event.getName();
		final ServiceInfo info = event.getDNS().getServiceInfo(event.getType(), event.getName());
	}

	@Override
	public void serviceRemoved(ServiceEvent event)
	{
		logger.debug("REMOVE: " + event.getName());
	}

	@Override
	public void serviceResolved(ServiceEvent event)
	{
		logger.info("RESOLVED: " + event.getInfo());
		final String serviceName = event.getName();
		final ServiceInfo serviceInfo = event.getInfo();

		final String code = database.findCode(serviceInfo.getName());
		if(code != null)
		{
			try
			{
				//TODO Not DONE!
				Session s = new Session(serviceInfo.getServer(), code);
			}
			catch(Exception e)
			{
				database.updateCode(serviceInfo.getName(), null);
			}
		}
	}

	@Override
	public void inetAddressAdded(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		InetAddress address = event.getInetAddress();
		logger.info("Registered Pairing Service @ " + address.getHostAddress());
		mdns.addServiceListener(IPairingResource.REMOTE_TYPE, this);
		mdns.addServiceListener(IRemoteControlResource.TOUCH_ABLE_TYPE, this);
		interfaces.put(mdns, address);
	}

	@Override
	public void inetAddressRemoved(NetworkTopologyEvent event)
	{
		JmDNS mdns = event.getDNS();
		mdns.removeServiceListener(IRemoteControlResource.TOUCH_ABLE_TYPE, this);
		mdns.removeServiceListener(IPairingResource.REMOTE_TYPE, this);
		mdns.unregisterAllServices();
		interfaces.remove(mdns);
	}

	// @Override
	// public void registerPairing(String hostname, String paircode, String servicename)
	// {
	// // ServiceInfo[] set1 = this.mDNS.list(IPairingResource.REMOTE_TYPE);
	// // ServiceInfo[] set2 = this.mDNS.list(IRemoteControlResource.TOUCH_ABLE_TYPE);
	// // Map<String, ServiceInfo[]> map = this.mDNS.listBySubtype("_tcp.local.");
	//
	// logger.debug(hostname + " " + paircode + " " + servicename);
	// // mDNS.requestServiceInfo(IRemoteControlResource.TOUCH_ABLE_TYPE, servicename);
	// Collection<ServiceInfo> serviceInfos = null;
	//
	// serviceInfos = Arrays.asList(mDNS.getServiceInfos(IRemoteControlResource.TOUCH_ABLE_TYPE, servicename));
	// if(serviceInfos.isEmpty())
	// {
	// serviceInfos = Arrays.asList(mDNS.getServiceInfos(IRemoteControlResource.DACP_TYPE, servicename));
	// }
	//
	// if(serviceInfos.isEmpty())
	// {
	// return;
	// }
	//
	// for(ServiceInfo serviceInfo : serviceInfos)
	// {
	// String libraryName = serviceInfo.getPropertyString("CtlN");
	// if(libraryName == null)
	// {
	// libraryName = serviceInfo.getName();
	// }
	// }
	// }
}
