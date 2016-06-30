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

	private static final Logger LOGGER = LoggerFactory.getLogger(MDNSResource.class);
	public final String hostname = InetAddress.getLocalHost().getHostName();
	
	public static final String MACHINE_ID_KEY = "Machine ID";
	public static final String DATABASE_ID_KEY = "Database ID";
	public static final String TXT_VERSION_KEY = "txtvers";
	public static final String TXT_VERSION = "1";
	public static final String ITSH_VERSION_KEY = "iTSh Version";
	public static final String VERSION_KEY = "Version";
	public static final String MACHINE_NAME_KEY = "Machine Name";
	
	public static final String DB_ID;
//	public static final String MACHINE_ID = "920C68E36298";
//	public static final String DATABASE_ID = DB_ID = "3B787C7DD4D136BB";
//	public static final String MID = "FAE42441153E483B";
	
	public static final String MACHINE_ID = "920C68E36298";
	public static final String DATABASE_ID = DB_ID = "3B787C7DD4D136BB";
	public static final String MID = "FAE42441153E483B";
	public static final String MID_0X = "0x" + MID;
	
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
