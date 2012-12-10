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
package org.dyndns.jkiddo.dmap;

import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;

public abstract class MDNSResource
{
	private JmmDNS mDNS;
	protected Integer port;

	protected String hostname = InetAddress.getLocalHost().getHostName();

	public MDNSResource(JmmDNS mDNS, Integer port) throws IOException
	{
		this.mDNS = mDNS;
		this.port = port;
		registerService();
	}

	void registerService() throws IOException
	{
		mDNS.registerService(getServiceToRegister());
	}

	abstract protected ServiceInfo getServiceToRegister();
}
