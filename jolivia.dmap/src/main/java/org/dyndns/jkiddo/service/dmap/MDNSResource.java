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

import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MDNSResource
{
	private final IZeroconfManager mDNS;
	protected Integer port;

	static final Logger logger = LoggerFactory.getLogger(MDNSResource.class);
	public final String hostname = InetAddress.getLocalHost().getHostName();
	private IZeroconfManager.ServiceInfo serviceInfo;

	public MDNSResource(final IZeroconfManager mDNS, final Integer port) throws IOException
	{
		this.mDNS = mDNS;
		this.port = port;
	}

	// http://www.dns-sd.org/ServiceTypes.html
	abstract protected IZeroconfManager.ServiceInfo getServiceInfoToRegister();

	public void deRegister()
	{
		this.mDNS.unregisterService(serviceInfo);
	}

	public synchronized void register() throws IOException
	{
		serviceInfo = getServiceInfoToRegister();
		mDNS.registerService(serviceInfo);
	}
}
