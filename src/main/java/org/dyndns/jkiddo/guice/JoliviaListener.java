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
package org.dyndns.jkiddo.guice;

import javax.jmdns.JmmDNS;

import org.dyndns.jkiddo.daap.server.ILibraryResource;
import org.dyndns.jkiddo.daap.server.LibraryManager;
import org.dyndns.jkiddo.daap.server.LibraryResource;
import org.dyndns.jkiddo.dacp.client.IPairingResource;
import org.dyndns.jkiddo.dacp.client.PairingDaemon;
import org.dyndns.jkiddo.dacp.client.PairingResource;
import org.dyndns.jkiddo.dacp.server.IRemoteControlResource;
import org.dyndns.jkiddo.dacp.server.RemoteControlResource;
import org.dyndns.jkiddo.dmap.DMAPInterface;
import org.dyndns.jkiddo.jetty.JoliviaExceptionMapper;
import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JoliviaListener extends GuiceServletContextListener
{
	private int hostingPort;

	public JoliviaListener(int port)
	{
		super();
		hostingPort = port;
	}

	@Override
	protected Injector getInjector()
	{
		return Guice.createInjector(new JerseyServletModule() {
			@Override
			protected void configureServlets()
			{
				Multibinder<IMusicStoreReader> multibinder = Multibinder.newSetBinder(binder(), IMusicStoreReader.class);
				multibinder.addBinding().to(DeskMusicStoreReader.class).asEagerSingleton();

				bind(Integer.class).annotatedWith(Names.named(LibraryResource.DAAP_PORT_NAME)).toInstance(hostingPort);
				bind(Integer.class).annotatedWith(Names.named(PairingResource.DACP_CLIENT_PORT_NAME)).toInstance(hostingPort);
				bind(Integer.class).annotatedWith(Names.named(RemoteControlResource.DACP_SERVER_PORT_NAME)).toInstance(hostingPort);

				bind(LibraryManager.class);
				bind(PairingDaemon.class).asEagerSingleton();
				bind(ILibraryResource.class).to(LibraryResource.class);
				bind(IPairingResource.class).to(PairingResource.class);
				bind(IRemoteControlResource.class).to(RemoteControlResource.class);

				bind(JoliviaExceptionMapper.class);
				bind(JmmDNS.class).toInstance(JmmDNS.Factory.getInstance());
				bind(DMAPInterface.class).asEagerSingleton();

				filter("*").through(ProxyFilter.class);
				serve("/*").with(GuiceContainer.class);
			}
		});
	}
}
